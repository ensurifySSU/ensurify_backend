package com.example.ensurify.dto.response;

import com.example.ensurify.domain.enums.ContractCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetContractDocumentInfoResponse {

    @Schema(description = "계약서 이름", nullable = false, example = "계약서1")
    private String name;
    @Schema(description = "계약 카테고리", nullable = false, example = "IRP")
    private ContractCategory category;
    @Schema(description = "키워드 목록", example = "[\"재무\", \"계약\", \"IRP\"]")
    private List<String> keywords;
}
