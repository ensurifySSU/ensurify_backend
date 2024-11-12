package com.example.inssurify.controller;

import com.example.inssurify.common.apiPayload.BasicResponse;
import com.example.inssurify.dto.response.GetClientInfoResponse;
import com.example.inssurify.dto.response.GetClientListResponse;
import com.example.inssurify.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ClientController {

    private final ClientService clientService;

    // 고객 정보 조회
    @GetMapping("/{clientId}")
    public BasicResponse<GetClientInfoResponse> getClientInfo(Principal principal, @PathVariable Long clientId){

        Long clerkId = Long.parseLong(principal.getName());

        GetClientInfoResponse clientInfo = clientService.getClientInfo(clerkId, clientId);

        log.info("고객 정보 조회: clientId={}", clientId);

        return BasicResponse.onSuccess(clientInfo);
    }

    // 고객 목록 조회
    @GetMapping
    public BasicResponse<GetClientListResponse.clientList> getClientList(Principal principal){

        Long clerkId = Long.parseLong(principal.getName());

        log.info("행원 정보 조회: clerkId={}", clerkId);

        GetClientListResponse.clientList clientList = clientService.getClientList(clerkId);

        return BasicResponse.onSuccess(clientList);
    }
}
