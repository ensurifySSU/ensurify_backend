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
    private String type; // join_room, offer, answer, candidate
    private Long roomId;
    private String data;
    private List<String> allUsers;
    private String receiver;
    private Object offer;
    private Object answer;
    private Object candidate;
    private Object sdp;

    public void setFrom(String from) {
        this.sender = from;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setOffer(Object offer) {
        this.offer = offer;
    }

    public void setAnswer(Object answer) {
        this.answer = answer;
    }

    public void setCandidate(Object candidate) {
        this.candidate = candidate;
    }

    public void setSdp(Object sdp) {
        this.sdp = sdp;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setAllUsers(List<String> allUsers) {
        this.allUsers = allUsers;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
