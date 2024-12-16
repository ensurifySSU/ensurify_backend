package com.example.ensurify.controller;

import com.example.ensurify.common.apiPayload.BasicResponse;
import com.example.ensurify.dto.request.CreateGuestAccountRequest;
import com.example.ensurify.dto.request.LoginRequest;
import com.example.ensurify.dto.response.CreateGuestAccountResponse;
import com.example.ensurify.dto.response.GetUserInfoResponse;
import com.example.ensurify.dto.response.LoginResponse;
import com.example.ensurify.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "User", description = "User 관련 API입니다.")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인 후 access 토큰을 발급 받습니다.")
    public BasicResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request) {

        LoginResponse response = userService.login(request);

        return BasicResponse.onSuccess(response);
    }

    @PostMapping("/guests/signup")
    @Operation(summary = "Guest 계정 생성", description = "Guest 계정(Client용)을 생성합니다.")
    public BasicResponse<CreateGuestAccountResponse> createGuestAccount(@RequestBody @Valid CreateGuestAccountRequest request) {

        CreateGuestAccountResponse response = userService.createGuestAccount(request.getClientId());

        return BasicResponse.onSuccess(response);
    }

    @GetMapping
    @Operation(summary = "행원 정보 조회", description = "행원(유저 본인) 정보를 조회합니다.")
    public BasicResponse<GetUserInfoResponse> getuserInfo(Principal principal){

        Long userId = Long.parseLong(principal.getName());

        GetUserInfoResponse userInfo = userService.getUserInfo(userId);

        log.info("행원 정보 조회: userId={}", userId);

        return BasicResponse.onSuccess(userInfo);
    }
}
