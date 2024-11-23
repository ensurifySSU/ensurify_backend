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
public class GetUserInfoResponse {

    @Schema(description = "행원명", nullable = false, example = "짱구")
    private String name;
    @Schema(description = "이미지 URL", nullable = false, example = "...")
    private String imageUrl;
}
