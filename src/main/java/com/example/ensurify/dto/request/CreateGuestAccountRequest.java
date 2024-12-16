package com.example.ensurify.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class CreateGuestAccountRequest {

    @Schema(description = "고객 id", nullable = false, example = "1")
    private Long clientId;
}
