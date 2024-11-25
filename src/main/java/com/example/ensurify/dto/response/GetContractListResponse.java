package com.example.ensurify.dto.response;

import com.example.ensurify.domain.enums.ContractCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class GetContractListResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class contractList {

        private List<contractInfo> contractList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class contractInfo {

        @Schema(description = "계약 id", nullable = false, example = "1")
        private Long contractId;
        @Schema(description = "계약서 이름", nullable = false, example = "계약서1")
        private String name;
        @Schema(description = "계약 카테고리", nullable = false, example = "IRP")
        private ContractCategory category;
        @Schema(description = "고객명", nullable = false, example = "짱구")
        private String client;
        @Schema(description = "청약일자", nullable = false, example = "2024.11.08")
        private String date;
        @Schema(description = "계약 내역 pdf", nullable = false, example = "...")
        private String pdfUrl;
    }
}
