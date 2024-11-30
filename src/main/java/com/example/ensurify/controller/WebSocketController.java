package com.example.ensurify.controller;

import com.example.ensurify.common.jwt.TokenProvider;
import com.example.ensurify.dto.request.CheckRequest;
import com.example.ensurify.dto.request.MovePageRequest;
import com.example.ensurify.dto.request.SignRequest;
import com.example.ensurify.service.WebSocketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import static com.example.ensurify.common.jwt.TokenAuthenticationFilter.HEADER_AUTHORIZATION;
import static com.example.ensurify.common.jwt.TokenAuthenticationFilter.getAccessToken;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final WebSocketService webSocketService;
    private final TokenProvider tokenProvider;

    // 체크 내역 송수신
    @MessageMapping("/check")
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


    // 서명 송수신
    @MessageMapping("/sign")
    public void sign(@Payload @Valid SignRequest request, @Header(HEADER_AUTHORIZATION) String authorizationHeader) {

        String accessToken = getAccessToken(authorizationHeader);
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        Long userId = Long.parseLong(authentication.getName());

        webSocketService.validMeetingRoom(request.getMeetingRoomId(), userId);
        webSocketService.validSign(request);

        // 메시지를 해당 회의실 구독자들에게 전송
        messagingTemplate.convertAndSend("/sub/meetingroom/" + request.getMeetingRoomId(), request);
        log.info("meetingRoom={}, signNum={}, imgUrl={}", request.getMeetingRoomId(), request.getSignNum(), request.getImgUrl());
    }

    // 페이지 번호 송수신(이동용)
    @MessageMapping("/page")
    public void movePage(@Payload @Valid MovePageRequest request, @Header(HEADER_AUTHORIZATION) String authorizationHeader) {

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
