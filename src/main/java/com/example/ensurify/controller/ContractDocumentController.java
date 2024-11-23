package com.example.ensurify.controller;

import com.example.ensurify.common.apiPayload.BasicResponse;
import com.example.ensurify.dto.response.GetContractDocumentInfoResponse;
import com.example.ensurify.dto.response.GetContractDocumentListResponse;
import com.example.ensurify.service.ContractDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contract-docs")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "ContractDocument", description = "ContractDocument 관련 API입니다.")
public class ContractDocumentController {

    private final ContractDocumentService contractDocumentService;

    @GetMapping
    @Operation(summary = "계약서 목록 조회", description = "계약서 목록을 조회합니다.")
    public BasicResponse<GetContractDocumentListResponse.contractDocumentList> getContractDocumentList(){

        GetContractDocumentListResponse.contractDocumentList contractList = contractDocumentService.getContractDocumentList();

        return BasicResponse.onSuccess(contractList);
    }

    @GetMapping("/{contractDocumentId}")
    @Operation(summary = "계약서 상세 조회", description = "계약서를 상세 조회합니다.")
    public BasicResponse<GetContractDocumentInfoResponse> getContractDocumentInfo(@PathVariable Long contractDocumentId){

        GetContractDocumentInfoResponse contractDocumentInfo = contractDocumentService.getContractDocumentInfo(contractDocumentId);

        log.info("계약서 상세 조회: contractDocId={}", contractDocumentId);

        return BasicResponse.onSuccess(contractDocumentInfo);
    }
}
