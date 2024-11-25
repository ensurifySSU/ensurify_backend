package com.example.ensurify.service;

import com.example.ensurify.common.apiPayload.code.status.ErrorStatus;
import com.example.ensurify.common.apiPayload.exception.GeneralException;
import com.example.ensurify.domain.User;
import com.example.ensurify.domain.MeetingRoom;
import com.example.ensurify.dto.request.CheckRequest;
import com.example.ensurify.dto.request.MovePageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WebSocketService {

    private final MeetingRoomService meetingRoomService;
    private final UserService userService;

    /**
     * 회의실 검증
     */
    @Transactional
    public void validMeetingRoom(Long meetingRoomId, Long userId) {

        User user = userService.findById(userId);

        // 1. MeetingRoom 조회: 없으면 예외를 던짐
        MeetingRoom meetingRoom = meetingRoomService.findById(meetingRoomId);

        // 2. userId가 해당 MeetingRoom에 속한 유저인지 확인
        boolean isUserInRoom = meetingRoom.getUserMeetingRooms().stream()
                .anyMatch(userMeetingRoom -> userMeetingRoom.getUser().equals(user));
        if (!isUserInRoom) {
            throw new GeneralException(ErrorStatus.USER_NOT_IN_ROOM);  // 계약 당사자가 아닌 유저이면 예외 발생
        }
    }

    /**
     * 체크 내역 검증
     */
    @Transactional
    public void validCheck(CheckRequest request) {

        MeetingRoom meetingRoom = meetingRoomService.findById(request.getMeetingRoomId());

        if(meetingRoom.getContractDocument().getCheckTotal() < request.getCheckNum())
            throw new GeneralException(ErrorStatus.CHECK_NUM_NOT_FOUND);
    }

    /**
     * 페이지 이동 검증
     */
    @Transactional
    public void validPage(MovePageRequest request) {

        MeetingRoom meetingRoom = meetingRoomService.findById(request.getMeetingRoomId());

        if(meetingRoom.getContractDocument().getPageTotal() < request.getPageNum())
            throw new GeneralException(ErrorStatus.CHECK_NUM_NOT_FOUND);
    }
}
