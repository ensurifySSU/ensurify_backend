package com.example.ensurify.controller;

import com.example.ensurify.common.apiPayload.BasicResponse;
import com.example.ensurify.dto.request.CreateContractRequest;
import com.example.ensurify.dto.response.CreateContractResponse;
import com.example.ensurify.service.MeetingRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Room", description = "Room(회의실) 관련 API입니다.")
public class RoomController {

    private final MeetingRoomService meetingRoomService;

    @PostMapping("/{roomId}")
    @Operation(summary = "회의실 입장(고객용)", description = "회의실에 입장합니다.")
    public BasicResponse<?> participateRoom(Principal principal,
                                                              @PathVariable Long roomId){

        Long userId = Long.parseLong(principal.getName());

        meetingRoomService.participateRoom(userId, roomId);

        return BasicResponse.onSuccess(null);
    }
}

