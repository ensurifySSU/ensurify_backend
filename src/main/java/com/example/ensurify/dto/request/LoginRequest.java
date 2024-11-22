package com.example.ensurify.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequest {

    @NotBlank
    @Schema(description = "아이디", nullable = false, example = "test1234")
    private String username;

    @NotBlank
    @Schema(description = "비밀번호", nullable = false, example = "test1234")
    private String password;
}
