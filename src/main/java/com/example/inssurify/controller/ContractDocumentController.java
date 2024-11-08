package com.example.inssurify.controller;

import com.example.inssurify.common.apiPayload.BasicResponse;
import com.example.inssurify.dto.response.GetContractDocumentInfoResponse;
import com.example.inssurify.dto.response.GetContractDocumentListResponse;
import com.example.inssurify.service.ContractDocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contract-docs")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ContractDocumentController {

    private final ContractDocumentService contractDocumentService;


    // 계약서 목록 조회
    @GetMapping
    public BasicResponse<GetContractDocumentListResponse.contractDocumentList> getContractDocumentList(@RequestHeader Long clerkId){

        GetContractDocumentListResponse.contractDocumentList contractList = contractDocumentService.getContractDocumentList(clerkId);

        return BasicResponse.onSuccess(contractList);
    }

    // 계약서 상세 조회
    @GetMapping("/{contractDocumentId}")
    public BasicResponse<GetContractDocumentInfoResponse> getContractDocumentInfo(@PathVariable Long contractDocumentId){

        GetContractDocumentInfoResponse contractDocumentInfo = contractDocumentService.getContractDocumentInfo(contractDocumentId);

        log.info("계약서 상세 조회: contractDocId={}", contractDocumentId);

        return BasicResponse.onSuccess(contractDocumentInfo);
    }
}
