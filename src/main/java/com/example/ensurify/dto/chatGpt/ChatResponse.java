package com.example.ensurify.dto.chatGpt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {

    @Schema(description = "ChatGPT 답변", nullable = false, example = "...")
    private String answer;
}
