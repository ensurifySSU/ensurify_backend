package com.example.ensurify.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class GetClientListResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class clientList {

        private List<clientInfo> clientList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class clientInfo {

        @Schema(description = "고객 id", nullable = false, example = "1")
        private Long clientId;
        @Schema(description = "고객명", nullable = false, example = "짱구")
        private String name;
        @Schema(description = "이메일", nullable = false, example = "test@example.com")
        private String email;
    }
}
