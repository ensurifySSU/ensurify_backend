package com.example.ensurify.service;

import com.example.ensurify.domain.Client;
import com.example.ensurify.domain.User;
import com.example.ensurify.domain.enums.Role;
import com.example.ensurify.dto.chatGpt.ChatGptRequest;
import com.example.ensurify.dto.chatGpt.ChatGptResponse;
import com.example.ensurify.dto.chatGpt.ChatRequest;
import com.example.ensurify.dto.chatGpt.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OpenAiService {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    private final RestTemplate template;
    private final UserService userService;

    public ChatResponse chat(Long userId, ChatRequest chatRequest) {

        // 맞춤형 답변을 위한 정보 추가
        String question = chatRequest.getQuestion();
        question += "(답변은 2줄 이내로)";

        User user = userService.findById(userId);
        if (user.getRole() == Role.GUEST) {
            Client client = user.getClient();
            String clientInfo = String.format("답변 대상 정보: 성별=%s, 직업=%s, 나이=%d",
                    client.isMale() ? "남자" : "여자", client.getJob(), client.getAge());
            question += "\n" + clientInfo;
        } else {
            question += "\n답변 대상 정보: 직업=은행원, 경력=3년차, 부서=추진(상담)팀";
        }

        log.info("AI 답변 생성: question={}", question);

        // GPT 연동
        ChatGptRequest request = new ChatGptRequest(model, question, 200);

        ChatGptResponse chatGPTResponse =  template.postForObject(apiURL, request, ChatGptResponse.class);
        String answer = chatGPTResponse.getChoices().get(0).getMessage().getContent();

        return ChatResponse.builder()
                .answer(answer)
                .build();
    }
}
