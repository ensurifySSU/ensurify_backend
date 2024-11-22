package com.example.ensurify.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    @Schema(description = "액세스 토큰", nullable = false, example = "adwa243124mopq...")
    private String accessToken;
}
