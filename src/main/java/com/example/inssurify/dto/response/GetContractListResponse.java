package com.example.inssurify.dto.response;

import com.example.inssurify.domain.enums.ContractCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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

        private Long contractId;
        private String name;
        private ContractCategory category;
        private String client;
        private String date;
    }
}
