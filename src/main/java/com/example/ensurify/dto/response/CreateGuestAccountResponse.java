package com.example.ensurify.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateGuestAccountResponse {

    @NotBlank
    @Schema(description = "아이디", nullable = false, example = "test1234")
    private String username;

    @NotBlank
    @Schema(description = "비밀번호", nullable = false, example = "test1234")
    private String password;
}
