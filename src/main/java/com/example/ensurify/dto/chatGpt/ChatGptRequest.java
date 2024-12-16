package com.example.ensurify.dto.chatGpt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatGptRequest {
    private String model;
    private List<Message> messages;
    @JsonProperty("max_tokens")
    private int maxTokens; //생성되는 응답의 최대 길이를 제한하는 토큰 수

    public ChatGptRequest(String model, String prompt, int maxTokens) {
        this.model = model;
        this.messages =  new ArrayList<>();
        this.messages.add(new Message("user", prompt));
        this.maxTokens = maxTokens;
    }
}
