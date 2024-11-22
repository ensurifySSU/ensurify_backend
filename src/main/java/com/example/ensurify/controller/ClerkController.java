package com.example.ensurify.controller;

import com.example.ensurify.common.apiPayload.BasicResponse;
import com.example.ensurify.dto.request.LoginRequest;
import com.example.ensurify.dto.response.GetClerkInfoResponse;
import com.example.ensurify.dto.response.LoginResponse;
import com.example.ensurify.service.ClerkService;
import jakarta.validation.Valid;
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

    // 로그인
    @PostMapping("/login")
    public BasicResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request) {

        LoginResponse response = clerkService.login(request);

        return BasicResponse.onSuccess(response);
    }

    // 행원 정보 조회
    @GetMapping
    public BasicResponse<GetClerkInfoResponse> getClerkInfo(Principal principal){

        Long clerkId = Long.parseLong(principal.getName());

        GetClerkInfoResponse clerkInfo = clerkService.getClerkInfo(clerkId);

        log.info("행원 정보 조회: clerkId={}", clerkId);

        return BasicResponse.onSuccess(clerkInfo);
    }
}
