package com.example.ensurify.controller;

import com.example.ensurify.common.apiPayload.BasicResponse;
import com.example.ensurify.domain.stomp.actions.Check;
import com.example.ensurify.dto.request.CheckRequest;
import com.example.ensurify.service.CheckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
@RequiredArgsConstructor
@Tag(name = "WebSocket", description = "WebSocket 관련 API입니다.")
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final CheckService checkService;

    // 채팅 메시지 수신 및 저장
    @MessageMapping("/check")
    @Operation(summary = "체크", description = "계약서 동의란에 체크합니다.")
    public void receiveMessage(CheckRequest request) {

        // 메시지를 해당 회의실 구독자들에게 전송
        messagingTemplate.convertAndSend("/sub/meetingroom/" + request.getMeetingRoomId(), request);
        checkService.save(request);
        log.info("check num: {}, meeting room: {}", request.getCheckNum(), request.getMeetingRoomId());
    }
}
