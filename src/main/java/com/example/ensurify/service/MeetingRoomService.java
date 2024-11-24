package com.example.ensurify.service;

import com.example.ensurify.common.apiPayload.code.status.ErrorStatus;
import com.example.ensurify.common.apiPayload.exception.GeneralException;
import com.example.ensurify.domain.Client;
import com.example.ensurify.domain.stomp.MeetingRoom;
import com.example.ensurify.repository.MeetingRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeetingRoomService {

    private final MeetingRoomRepository meetingRoomRepository;

    // id로 meeting room 검색
    public MeetingRoom findById(Long roomId) {
        return meetingRoomRepository.findById(roomId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEETING_ROOM_NOT_FOUND));
    }
}
