package com.example.ensurify.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CheckRequest {

    private Long meetingRoomId;
    private int checkNum;
    @JsonProperty("isChecked")
    private boolean isChecked;
}
