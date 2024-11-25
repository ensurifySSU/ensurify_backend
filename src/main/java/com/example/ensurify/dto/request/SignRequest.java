package com.example.ensurify.dto.request;

import lombok.Getter;

@Getter
public class SignRequest {
    private Long meetingRoomId;
    private int signNum;
    private String imgUrl;
}
