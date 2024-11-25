package com.example.ensurify.controller;

import com.example.ensurify.common.jwt.TokenProvider;
import com.example.ensurify.dto.request.CheckRequest;
import com.example.ensurify.dto.request.MovePageRequest;
import com.example.ensurify.service.WebSocketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import static com.example.ensurify.common.jwt.TokenAuthenticationFilter.HEADER_AUTHORIZATION;
import static com.example.ensurify.common.jwt.TokenAuthenticationFilter.getAccessToken;

@Slf4j
@Controller
@RequiredArgsConstructor
@Tag(name = "WebSocket", description = "WebSocket 관련 API입니다.")
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final WebSocketService webSocketService;
    private final TokenProvider tokenProvider;

    // 체크 내역 수신
    @MessageMapping("/check")
    @Operation(summary = "체크", description = "계약서 동의란에 체크합니다.")
    public void check(@Payload @Valid CheckRequest request, @Header(HEADER_AUTHORIZATION) String authorizationHeader) {

        String accessToken = getAccessToken(authorizationHeader);
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        Long userId = Long.parseLong(authentication.getName());

        webSocketService.validMeetingRoom(request.getMeetingRoomId(), userId);
        webSocketService.validCheck(request);

        // 메시지를 해당 회의실 구독자들에게 전송
        messagingTemplate.convertAndSend("/sub/meetingroom/" + request.getMeetingRoomId(), request);
        log.info("meetingRoom={}, checkNum={}, checked={}", request.getMeetingRoomId(), request.getCheckNum(), request.isChecked());
    }

    // 페이지 번호 수신
    @MessageMapping("/page")
    @Operation(summary = "페이지 이동", description = "페이지 번호를 통해 이동합니다.")
    public void movePage(@Payload MovePageRequest request, @Header(HEADER_AUTHORIZATION) String authorizationHeader) {

        String accessToken = getAccessToken(authorizationHeader);
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        Long userId = Long.parseLong(authentication.getName());

        webSocketService.validMeetingRoom(request.getMeetingRoomId(), userId);
        webSocketService.validPage(request);

        // 메시지를 해당 회의실 구독자들에게 전송
        messagingTemplate.convertAndSend("/sub/meetingroom/" + request.getMeetingRoomId(), request);
        log.info("meetingRoom={}, pageNum={}", request.getMeetingRoomId(), request.getPageNum());
    }
}
