package com.example.ensurify.controller;

import com.example.ensurify.common.apiPayload.BasicResponse;
import com.example.ensurify.dto.response.GetClientInfoResponse;
import com.example.ensurify.dto.response.GetClientListResponse;
import com.example.ensurify.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Client", description = "Client 관련 API입니다.")
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/{clientId}")
    @Operation(summary = "고객 정보 조회", description = "고객 정보를 조회합니다.")
    public BasicResponse<GetClientInfoResponse> getClientInfo(@PathVariable Long clientId){

        GetClientInfoResponse clientInfo = clientService.getClientInfo(clientId);

        log.info("고객 정보 조회: clientId={}", clientId);

        return BasicResponse.onSuccess(clientInfo);
    }

    @GetMapping
    @Operation(summary = "고객 목록 조회", description = "고객 목록을 조회합니다.")
    public BasicResponse<GetClientListResponse.clientList> getClientList(){

        GetClientListResponse.clientList clientList = clientService.getClientList();

        return BasicResponse.onSuccess(clientList);
    }
}
