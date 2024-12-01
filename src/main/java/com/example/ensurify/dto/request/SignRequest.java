package com.example.ensurify.dto.request;

import lombok.Getter;

@Getter
public class SignRequest {
    private Long roomId;
    private int signNum;
    private String imgUrl;
}
