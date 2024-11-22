package com.example.ensurify.controller;

import com.example.ensurify.common.apiPayload.BasicResponse;
import com.example.ensurify.dto.response.GetContractDocumentInfoResponse;
import com.example.ensurify.dto.response.GetContractDocumentListResponse;
import com.example.ensurify.service.ContractDocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/contract-docs")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ContractDocumentController {

    private final ContractDocumentService contractDocumentService;


    // 계약서 목록 조회
    @GetMapping
    public BasicResponse<GetContractDocumentListResponse.contractDocumentList> getContractDocumentList(Principal principal){

        Long clerkId = Long.parseLong(principal.getName());

        GetContractDocumentListResponse.contractDocumentList contractList = contractDocumentService.getContractDocumentList(clerkId);

        return BasicResponse.onSuccess(contractList);
    }

    // 계약서 상세 조회
    @GetMapping("/{contractDocumentId}")
    public BasicResponse<GetContractDocumentInfoResponse> getContractDocumentInfo(Principal principal, @PathVariable Long contractDocumentId){

        Long clerkId = Long.parseLong(principal.getName());

        GetContractDocumentInfoResponse contractDocumentInfo = contractDocumentService.getContractDocumentInfo(clerkId, contractDocumentId);

        log.info("계약서 상세 조회: contractDocId={}", contractDocumentId);

        return BasicResponse.onSuccess(contractDocumentInfo);
    }
}
