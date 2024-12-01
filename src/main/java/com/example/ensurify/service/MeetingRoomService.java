package com.example.ensurify.service;

import com.example.ensurify.common.apiPayload.code.status.ErrorStatus;
import com.example.ensurify.common.apiPayload.exception.GeneralException;
import com.example.ensurify.domain.*;
import com.example.ensurify.domain.enums.Role;
import com.example.ensurify.dto.request.CreateContractRequest;
import com.example.ensurify.dto.response.CreateContractResponse;
import com.example.ensurify.repository.MeetingRoomRepository;
import com.example.ensurify.repository.UserMeetingRoomRepository;
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
    private final UserMeetingRoomRepository userMeetingRoomRepository;
    private final UserService userService;
    private final ContractDocumentService contractDocumentService;

    // 회의실 생성
    @Transactional
    public Long createRoom(Long clerkId, Long contractDocumentId) {

        User user = userService.findById(clerkId);

        if(user.getRole() != Role.USER)
            throw new GeneralException(ErrorStatus.USER_ACCESS_ONLY);

        ContractDocument document = contractDocumentService.findById(contractDocumentId);

        MeetingRoom room = MeetingRoom.builder()
                .contractDocument(document)
                .build();

        meetingRoomRepository.save(room);

        UserMeetingRoom userMeetingRoom = UserMeetingRoom.builder()
                .user(user)
                .meetingRoom(room)
                .build();

        userMeetingRoomRepository.save(userMeetingRoom);

        return room.getId();
    }

    // 회의실 입장
    @Transactional
    public void participateRoom(Long clientId, Long roomId) {

        User user = userService.findById(clientId);

        if(user.getRole() != Role.GUEST)
            throw new GeneralException(ErrorStatus.GUEST_ACCESS_ONLY);

        MeetingRoom room = findById(roomId);

        UserMeetingRoom userMeetingRoom = UserMeetingRoom.builder()
                .user(user)
                .meetingRoom(room)
                .build();

        userMeetingRoomRepository.save(userMeetingRoom);
    }

    // id로 meeting room 검색
    public MeetingRoom findById(Long roomId) {
        return meetingRoomRepository.findById(roomId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEETING_ROOM_NOT_FOUND));
    }
}
