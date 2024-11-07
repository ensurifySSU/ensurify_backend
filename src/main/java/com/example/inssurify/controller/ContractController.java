package com.example.inssurify.controller;

import com.example.inssurify.common.apiPayload.BasicResponse;
import com.example.inssurify.dto.request.CreateContractRequest;
import com.example.inssurify.dto.response.CreateContractResponse;
import com.example.inssurify.service.ContractService;
import jakarta.validation.Valid;
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

    // 계약 생성
    @PostMapping
    public BasicResponse<CreateContractResponse> postContract(@RequestHeader Long clerkId,
                                                              @RequestBody @Valid CreateContractRequest request){

        CreateContractResponse response = contractService.createContract(clerkId, request);

        return BasicResponse.onSuccess(response);
    }
}

