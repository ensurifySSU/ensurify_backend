package com.example.inssurify.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetClerkInfoResponse {

    private String name;
    private String bank;
    private String imageUrl;
}
