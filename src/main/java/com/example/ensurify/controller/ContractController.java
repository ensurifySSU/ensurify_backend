package com.example.ensurify.controller;

import com.example.ensurify.common.apiPayload.BasicResponse;
import com.example.ensurify.dto.request.CreateContractRequest;
import com.example.ensurify.dto.response.CreateContractResponse;
import com.example.ensurify.dto.response.GetContractListResponse;
import com.example.ensurify.service.ContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Contract", description = "Contract 관련 API입니다.")
public class ContractController {

    private final ContractService contractService;

    @PostMapping
    @Operation(summary = "계약 생성", description = "계약을 생성합니다.")
    public BasicResponse<CreateContractResponse> postContract(Principal principal,
                                                              @RequestBody @Valid CreateContractRequest request){

        Long clerkId = Long.parseLong(principal.getName());

        CreateContractResponse response = contractService.createContract(clerkId, request);

        return BasicResponse.onSuccess(response);
    }

    @GetMapping
    @Operation(summary = "계약 목록 조회", description = "계약 목록을 조회합니다.")
    public BasicResponse<GetContractListResponse.contractList> getContractList(Principal principal,
                                                                               @RequestParam(required = false) Long docId){

        Long clerkId = Long.parseLong(principal.getName());

        GetContractListResponse.contractList contractList = contractService.getContractList(clerkId, docId);

        return BasicResponse.onSuccess(contractList);
    }
}

