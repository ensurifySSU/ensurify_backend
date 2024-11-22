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
public class GetClientInfoResponse {

    @Schema(description = "고객명", nullable = false, example = "짱구")
    private String name;
    @Schema(description = "이메일", nullable = false, example = "test@example.com")
    private String email;
    @Schema(description = "연령", nullable = false, example = "25")
    private int age;
    @Schema(description = "성별", nullable = false, example = "남")
    private String gender;
}
