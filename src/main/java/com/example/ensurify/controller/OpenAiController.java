package com.example.ensurify.controller;

import com.example.ensurify.common.apiPayload.BasicResponse;
import com.example.ensurify.dto.chatGpt.ChatGptRequest;
import com.example.ensurify.dto.chatGpt.ChatGptResponse;
import com.example.ensurify.dto.chatGpt.ChatRequest;
import com.example.ensurify.dto.chatGpt.ChatResponse;
import com.example.ensurify.service.OpenAiService;
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

    private final OpenAiService openAiService;

    @PostMapping("/chat")
    public BasicResponse<ChatResponse> chat(@RequestBody @Valid ChatRequest chatRequest, Principal principal){
        Long userId = Long.parseLong(principal.getName());

        ChatResponse response = openAiService.chat(userId, chatRequest);

        return BasicResponse.onSuccess(response);
    }
}
