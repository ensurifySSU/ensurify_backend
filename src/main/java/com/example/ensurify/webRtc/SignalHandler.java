package com.example.ensurify.webRtc;

import com.example.ensurify.dto.response.StompResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;
import java.util.*;

// 기능 : WebRTC를 위한 시그널링 서버 부분으로 요청타입에 따라 분기 처리(Controller + Service)
@Slf4j
@Service
@RequiredArgsConstructor
public class SignalHandler extends TextWebSocketHandler {

    private final SessionRepository sessionRepository;
    private final ObjectMapper objectMapper;
    private static final String MSG_TYPE_JOIN_ROOM = "join_room";
    private static final String MSG_TYPE_OFFER = "offer";
    private static final String MSG_TYPE_ANSWER = "answer";
    private static final String MSG_TYPE_CANDIDATE = "candidate";

    // 웹소켓 연결이 성공적으로 이루어지면 호출되는 메소드
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String sessionId = session.getId();

        StompResponse response = StompResponse.builder()
                .sessionId(sessionId)
                .build();

        try {
            // 클라이언트에 응답 메시지(JSON) 전송
            String jsonResponse = objectMapper.writeValueAsString(response);
            session.sendMessage(new TextMessage(jsonResponse));
            log.info("연결 성공, session id={}", session.getId());

        } catch (IOException e) {
            log.error("연결 오류, session ID: {}", e.getMessage());
        }
    }

    // 클라이언트로부터 받은 텍스트 메시지를 처리하는 메소드
    @Override
    protected void handleTextMessage(final WebSocketSession session, final TextMessage textMessage) {

        try {
            // 받은 텍스트 메시지를 WebSocketMessage 객체로 변환
            WebSocketMessage message = objectMapper.readValue(textMessage.getPayload(), WebSocketMessage.class);
            String userName = message.getSender();
            Long roomId = message.getRoomId();

            log.info("Message Info: sessionId={}, getType={}, getRoomId={}", session.getId(), message.getType(), roomId.toString());

            // 메시지 타입에 따라 분기 처리
            switch (message.getType()) {
                case MSG_TYPE_JOIN_ROOM:
                    // 방에 입장하는 메시지 처리
                    handleJoinRoom(session, message, roomId, userName);
                    break;
                case MSG_TYPE_OFFER:
                case MSG_TYPE_ANSWER:
                case MSG_TYPE_CANDIDATE:
                    // WebRTC 메시지 처리 : offer, answer, candidate
                    handleOfferAnswerCandidate(session, message, roomId);
                    break;
                default:
                    log.warn("알 수 없는 메시지 타입: {}", message.getType());
            }
        } catch (JsonProcessingException e) {
            log.error("메시지 처리 중 오류 발생, 해당 메시지를 무시합니다. sessionId={}, error={}", session.getId(), e.getMessage());
            sendMessage(session, WebSocketMessage.builder()
                    .type("error")
                    .data("잘못된 메시지 형식입니다.")
                    .build());
        } catch (Exception e) {
            log.error("메시지 처리 중 예기치 않은 오류 발생, sessionId={}, error={}", session.getId(), e.getMessage());
            sendMessage(session, WebSocketMessage.builder()
                    .type("error")
                    .data("잘못된 메시지 형식입니다.")
                    .build());
        }
    }

    // 방에 입장하는 메시지를 처리하는 메소드
    private void handleJoinRoom(WebSocketSession session, @Valid WebSocketMessage message, Long roomId, String userName) {
        log.info("[{}]", message.getType());
        try {
            if (sessionRepository.hasRoom(roomId)) {
                // 방이 존재하면 해당 방에 클라이언트 추가
                log.info("방 {}에 입장 - 기존 참가자들: {}", roomId, sessionRepository.getClientList(roomId).keySet());
                sessionRepository.addClient(roomId, session);
            } else {
                // 방이 존재하지 않으면 새 방 생성 후 클라이언트 추가
                log.info("방 {}에 입장 - 새로운 방 생성", roomId);
                sessionRepository.addClientInNewRoom(roomId, session);
            }

            // 세션에 해당 방 ID 저장
            sessionRepository.saveRoomIdToSession(session, roomId);

            // 방에 입장한 다른 참가자들의 ID를 리스트로 생성
            List<String> exportClientList = new ArrayList<>();
            for (Map.Entry<String, WebSocketSession> entry : sessionRepository.getClientList(roomId).entrySet()) {
                if (entry.getValue() != session) {
                    exportClientList.add(entry.getKey());
                }
            }

            // 방에 입장한 사용자들에게 메시지 전송
            log.info("방 {}에 입장한 사용자 목록: {}", roomId, exportClientList);
            sendMessage(session, WebSocketMessage.builder()
                    .type("all_users")
                    .sender(userName)
                    .data(message.getData())  // 메시지 데이터
                    .allUsers(exportClientList)  // 방 참가자 리스트
                    .candidate(message.getCandidate())  // ICE 후보
                    .sdp(message.getSdp())  // SDP
                    .build());
        } catch (Exception e) {
            log.error("방 입장 처리 중 오류 발생", e);  // 예외 발생 시 로그
        }
    }

    // WebRTC의 offer, answer, candidate 메시지를 처리하는 메소드
    private void handleOfferAnswerCandidate(WebSocketSession session, @Valid WebSocketMessage message, Long roomId) {
        log.info("[{}]", message.getType());
        try {
            if (sessionRepository.hasRoom(roomId)) {
                Map<String, WebSocketSession> clientList = sessionRepository.getClientList(roomId);
                log.info("보내는 사람: {}, 받는 사람: {}", session.getId(), message.getReceiver());

                // 받는 사람의 세션이 존재하는 경우, 해당 세션으로 메시지 전달
                if (clientList.containsKey(message.getReceiver())) {
                    WebSocketSession receiverSession = clientList.get(message.getReceiver());
                    sendMessage(receiverSession, WebSocketMessage.builder()
                            .type(message.getType())  // 메시지 타입
                            .sender(session.getId())  // 보낸 사람의 session ID
                            .receiver(message.getReceiver())  // 받는 사람의 session ID
                            .data(message.getData())  // 메시지 데이터
                            .offer(message.getOffer())  // WebRTC offer
                            .answer(message.getAnswer())  // WebRTC answer
                            .candidate(message.getCandidate())  // WebRTC ICE 후보
                            .sdp(message.getSdp())  // SDP
                            .build());
                } else {
                    log.warn("받는 사람의 세션을 찾을 수 없습니다. 메시지 받지 않음: {}", message.getReceiver());
                }
            } else {
                log.warn("방 {}가 존재하지 않음. 메시지를 처리할 수 없습니다.", roomId);
            }
        } catch (Exception e) {
            log.error("Offer/Answer/Candidate 처리 중 오류 발생", e);  // 예외 발생 시 로그중
        }
    }

    // 웹소켓 연결이 종료되었을 때 호출되는 메소드
    @Override
    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) {
        try {
            log.info("연결 종료 - session ID: {}", session.getId());

            Long roomId = Optional.ofNullable(sessionRepository.getRoomId(session))
                    .orElseThrow(() -> new IllegalArgumentException("해당 세션이 속한 방 정보를 찾을 수 없음"));

            // 방에서 클라이언트 삭제
            sessionRepository.deleteClient(roomId, session);
            // 방에 대한 세션 정보 삭제
            sessionRepository.deleteRoomIdToSession(session);

            // 방에 있는 다른 클라이언트들에게 'leave' 메시지 전송
            Map<String, WebSocketSession> clientList = Optional.ofNullable(sessionRepository.getClientList(roomId))
                    .orElseThrow(() -> new IllegalArgumentException("클라이언트 목록을 찾을 수 없음"));

            for (Map.Entry<String, WebSocketSession> oneClient : clientList.entrySet()) {
                sendMessage(oneClient.getValue(), WebSocketMessage.builder()
                        .type("leave")
                        .sender(session.getId())  // 나가는 사람의 ID
                        .receiver(oneClient.getKey())  // 상대방의 ID
                        .build());
            }

        } catch (Exception e) {
            log.error("연결 종료 처리 중 오류 발생", e);  // 예외 발생 시 로그
        }
    }

    // 메세지 발송
    private void sendMessage(WebSocketSession session, WebSocketMessage message) {
        try {
            // 메시지를 JSON으로 변환하여 전송
            String json = objectMapper.writeValueAsString(message);
            log.info("메시지 발송 - 대상: {}, 내용: {}", session.getId(), json);
            session.sendMessage(new TextMessage(json));
        } catch (IOException e) {
            log.error("메시지 전송 중 오류 발생", e);
        }
    }
}