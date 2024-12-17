package com.example.ensurify.webRtc;

import com.example.ensurify.common.apiPayload.code.status.ErrorStatus;
import com.example.ensurify.common.apiPayload.exception.GeneralException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

// 기능 : 웹소켓에 필요한 세션 정보를 저장, 관리 (싱글톤)(Repository)
@Slf4j
@Component
@NoArgsConstructor
public class SessionRepository {
    // 방 ID를 키로, 각 방의 세션 정보(session Id & Object)를 저장(역할: 각 방에 속한 클라이언트 세션 정보를 저장)
    private final Map<Long, Map<String, WebSocketSession>> clientsInRoom = new HashMap<>();

    // 세션을 키로, 세션이 속한 방 ID를 저장(역할: 특정 세션이 어느 방에 속해 있는지를 추적)
    private final Map<WebSocketSession, Long> roomIdToSession = new HashMap<>();

    // 특정 방에 속한 클라이언트 목록을 조회
    public Map<String, WebSocketSession> getClientList(Long roomId) {
        if (!clientsInRoom.containsKey(roomId)) {
            throw new GeneralException(ErrorStatus.MEETING_ROOM_NOT_FOUND);
        }
        return clientsInRoom.get(roomId);
    }

    // 특정 방의 존재 여부를 확인
    public boolean hasRoom(Long roomId){
        return clientsInRoom.containsKey(roomId);
    }

    // 해당 session이 어느 방에 있는지 조회
    public Long getRoomId(WebSocketSession session){
        return roomIdToSession.get(session);
    }

    // 새로운 방을 생성하고, 해당 방에 클라이언트를 추가
    public void addClientInNewRoom(Long roomId, WebSocketSession session) {
        Map<String, WebSocketSession> newClient = new HashMap<>();
        newClient.put(session.getId(), session);
        clientsInRoom.put(roomId, newClient);
    }

    // 기존 방에 새로운 클라이언트를 추가
    public void addClient(Long roomId, WebSocketSession session) {
        clientsInRoom.get(roomId).put(session.getId(), session);
    }

    // 특정 클라이언트를 방에서 제거
    public void deleteClient(Long roomId, WebSocketSession session) {
        Map<String, WebSocketSession> clientList = clientsInRoom.get(roomId);
        String removeKey = "";
        for(Map.Entry<String, WebSocketSession> oneClient : clientList.entrySet()){
            if(oneClient.getKey().equals(session.getId())){
                removeKey = oneClient.getKey();
            }
        }
        log.info("회의실 내 유저 삭제: sessionId={}", removeKey);
        clientList.remove(removeKey);

        // 끊어진 세션을 제외한 나머지 세션들을 다시 저장
        clientsInRoom.put(roomId, clientList);
    }

    // 특정 방을 삭제하고, 그 안의 모든 클라이언트를 제거
    public void deleteAllClientsInRoom(Long roomId){
        clientsInRoom.remove(roomId);
        log.info("Room deleted: {}", roomId);
    }

    // 특정 방에 속한 세션 목록을 조회
    public Map<WebSocketSession, Long> searchRoomIdToSession(Long roomId) {
        return Optional.of(roomIdToSession.entrySet()
                        .stream()
                        .filter(entry -> Objects.equals(entry.getValue(), roomId))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
                .orElseThrow(
                        () -> new IllegalArgumentException("RoodIdToSession 정보 없음")
                );
    }

    // 특정 세션이 어느 방에 속해 있는지를 저장
    public void saveRoomIdToSession(WebSocketSession session, Long roomId) {
        roomIdToSession.put(session, roomId);
    }

    // 특정 세션의 방 ID 데이터를 삭제
    public void deleteRoomIdToSession(WebSocketSession session) {
        roomIdToSession.remove(session);
    }
}
