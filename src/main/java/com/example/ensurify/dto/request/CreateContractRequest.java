package com.example.ensurify.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class CreateContractRequest {

    @Schema(description = "계약서 id", nullable = false, example = "1")
    private Long contractDocumentId;
    @Schema(description = "고객 id", nullable = false, example = "1")
    private Long clientId;
}
