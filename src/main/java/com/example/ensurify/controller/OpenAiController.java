package com.example.ensurify.controller;

import com.example.ensurify.common.apiPayload.BasicResponse;
import com.example.ensurify.dto.chatGpt.ChatGptRequest;
import com.example.ensurify.dto.chatGpt.ChatGptResponse;
import com.example.ensurify.dto.chatGpt.ChatRequest;
import com.example.ensurify.dto.chatGpt.ChatResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;

@RestController
@RequestMapping("/openai")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "OpenAI", description = "OpenAI 관련 API입니다.")
public class OpenAiController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    private RestTemplate template;

    @PostMapping("/chat")
    public BasicResponse<ChatResponse> chat(@RequestBody @Valid ChatRequest chatRequest, Principal principal){
        Long userId = Long.parseLong(principal.getName());


        ChatGptRequest request = new ChatGptRequest(model, chatRequest.getQuestion());
        ChatGptResponse chatGPTResponse =  template.postForObject(apiURL, request, ChatGptResponse.class);
        String answer = chatGPTResponse.getChoices().get(0).getMessage().getContent();

        ChatResponse response = ChatResponse.builder()
                .answer(answer)
                .build();

        return BasicResponse.onSuccess(response);
    }
}
