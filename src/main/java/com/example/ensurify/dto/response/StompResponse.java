package com.example.ensurify.dto.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StompResponse {
    private String sessionId;
}
