package com.example.ensurify.dto.chatGpt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ChatRequest {

    @Schema(description = "질문(프롬프트)", nullable = false, example = "IRP란?")
    private String question;
}
