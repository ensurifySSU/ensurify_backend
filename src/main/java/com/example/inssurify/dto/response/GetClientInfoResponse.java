package com.example.inssurify.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetClientInfoResponse {

    private String name;
    private String email;
    private int age;
    private String gender;
}
