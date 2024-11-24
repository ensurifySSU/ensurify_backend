package com.example.ensurify.service;

import com.example.ensurify.common.apiPayload.code.status.ErrorStatus;
import com.example.ensurify.common.apiPayload.exception.GeneralException;
import com.example.ensurify.domain.stomp.MeetingRoom;
import com.example.ensurify.domain.stomp.actions.Check;
import com.example.ensurify.dto.request.CheckRequest;
import com.example.ensurify.repository.CheckRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CheckService {

    private final CheckRepository checkRepository;
    private final MeetingRoomService meetingRoomService;

    /**
     * 체크 내역 저장
     */
    @Transactional
    public void save(CheckRequest request) {

        MeetingRoom meetingRoom = meetingRoomService.findById(request.getMeetingRoomId());

        if(meetingRoom.getContractDocument().getCheckTotal() < request.getCheckNum())
            throw new GeneralException(ErrorStatus.CHECK_NUM_NOT_FOUND);

        Check check = Check.builder()
                .checkNum(request.getCheckNum())
                .meetingRoom(meetingRoom)
                .build();

        checkRepository.save(check);
    }
}
