package com.example.ensurify.dto.response;

import com.example.ensurify.domain.enums.ContractCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class GetContractDocumentListResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class contractDocumentList {

        private List<contractDocumentInfo> contractDocumentList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class contractDocumentInfo {

        private Long contractDocumentId;
        private String name;
        private ContractCategory category;
    }
}
