package com.example.ensurify.webRtc;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

// 기능 : 프론트에 응답하는 시그널링용 Message
@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketMessage {
    private String sender;
    private String type; // join_room, offer, answer, candidate, leave
    private Long roomId;
    private String data;
    private List<String> allUsers;
    private String receiver;
    private Object offer;
    private Object answer;
    private Object candidate;
    private Object sdp;
}
