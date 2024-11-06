package com.example.inssurify.controller;

import com.example.inssurify.common.apiPayload.BasicResponse;
import com.example.inssurify.dto.response.GetClientListResponse;
import com.example.inssurify.dto.response.GetContractListResponse;
import com.example.inssurify.service.ContractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contracts")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ContractController {

    private final ContractService contractService;


    // 계약서 목록 조회
    @GetMapping
    public BasicResponse<GetContractListResponse.contractList> getClientList(@RequestHeader Long clerkId){

        GetContractListResponse.contractList contractList = contractService.getContractList(clerkId);

        return BasicResponse.onSuccess(contractList);
    }
}
