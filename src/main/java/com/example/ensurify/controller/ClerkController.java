package com.example.ensurify.controller;

import com.example.ensurify.common.apiPayload.BasicResponse;
import com.example.ensurify.dto.request.LoginRequest;
import com.example.ensurify.dto.response.GetClerkInfoResponse;
import com.example.ensurify.dto.response.LoginResponse;
import com.example.ensurify.service.ClerkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Clerk", description = "Clerk 관련 API입니다.")
public class ClerkController {

    private final ClerkService clerkService;

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인 후 access 토큰을 발급 받습니다.")
    public BasicResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request) {

        LoginResponse response = clerkService.login(request);

        return BasicResponse.onSuccess(response);
    }

    @GetMapping
    @Operation(summary = "행원 정보 조회", description = "행원(유저 본인) 정보를 조회합니다.")
    public BasicResponse<GetClerkInfoResponse> getClerkInfo(Principal principal){

        Long clerkId = Long.parseLong(principal.getName());

        GetClerkInfoResponse clerkInfo = clerkService.getClerkInfo(clerkId);

        log.info("행원 정보 조회: clerkId={}", clerkId);

        return BasicResponse.onSuccess(clerkInfo);
    }
}
