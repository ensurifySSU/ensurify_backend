package com.example.inssurify.controller;

import com.example.inssurify.common.apiPayload.BasicResponse;
import com.example.inssurify.dto.request.CreateContractRequest;
import com.example.inssurify.dto.response.CreateContractResponse;
import com.example.inssurify.dto.response.GetContractListResponse;
import com.example.inssurify.service.ContractService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/contracts")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ContractController {

    private final ContractService contractService;

    // 계약 생성
    @PostMapping
    public BasicResponse<CreateContractResponse> postContract(Principal principal,
                                                              @RequestBody @Valid CreateContractRequest request){

        Long clerkId = Long.parseLong(principal.getName());

        CreateContractResponse response = contractService.createContract(clerkId, request);

        return BasicResponse.onSuccess(response);
    }

    // 계약 목록 조회
    @GetMapping
    public BasicResponse<GetContractListResponse.contractList> getContractList(Principal principal,
                                                                               @RequestParam(required = false) Long docId){

        Long clerkId = Long.parseLong(principal.getName());

        GetContractListResponse.contractList contractList = contractService.getContractList(clerkId, docId);

        return BasicResponse.onSuccess(contractList);
    }
}

