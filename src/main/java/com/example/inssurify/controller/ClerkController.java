package com.example.inssurify.controller;

import com.example.inssurify.common.apiPayload.BasicResponse;
import com.example.inssurify.dto.response.GetClerkInfoResponse;
import com.example.inssurify.service.ClerkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/clerks")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ClerkController {

    private final ClerkService clerkService;

    // 행원 정보 조회
    @GetMapping
    public BasicResponse<GetClerkInfoResponse> getClerkInfo(@RequestHeader Long clerkId){

        GetClerkInfoResponse clerkInfo = clerkService.getClerkInfo(clerkId);

        log.info("행원 정보 조회: clerkId={}", clerkId);

        return BasicResponse.onSuccess(clerkInfo);
    }
}
