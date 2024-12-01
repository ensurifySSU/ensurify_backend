package com.example.ensurify.controller;

import com.example.ensurify.common.apiPayload.BasicResponse;
import com.example.ensurify.dto.request.CreateContractRequest;
import com.example.ensurify.dto.response.*;
import com.example.ensurify.service.ContractDocumentService;
import com.example.ensurify.service.ContractHistoryService;
import com.example.ensurify.service.MeetingRoomService;
import com.example.ensurify.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/contracts")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Contract", description = "Contract 관련 API입니다.")
public class ContractController {

    private final ContractHistoryService contractHistoryService;
    private final ContractDocumentService contractDocumentService;
    private final MeetingRoomService meetingRoomService;
    private final S3Service s3Service;

    @PostMapping
    @Operation(summary = "계약 생성", description = "계약을 생성합니다.")
    public BasicResponse<CreateContractResponse> postContract(Principal principal,
                                                              @RequestBody @Valid CreateContractRequest request){

        Long userId = Long.parseLong(principal.getName());

        Long roomId = meetingRoomService.createRoom(userId, request.getContractDocumentId());

        CreateContractResponse response = contractHistoryService.createContract(userId, request, roomId);

        return BasicResponse.onSuccess(response);
    }

    @GetMapping("/history")
    @Operation(summary = "계약 내역 목록 조회", description = "계약 내역 목록을 조회합니다.")
    public BasicResponse<GetContractListResponse.contractList> getContractList(Principal principal,
                                                                               @RequestParam(required = false) Long docId){

        Long userId = Long.parseLong(principal.getName());

        GetContractListResponse.contractList contractList = contractHistoryService.getContractList(userId, docId);

        return BasicResponse.onSuccess(contractList);
    }

    @PostMapping("/{contractId}/pdf")
    @Operation(summary = "계약 내역 PDF 저장", description = "계약 내역 PDF을 저장합니다.")
    public BasicResponse<PostContractPdfResponse> postContractPDF(Principal principal,
                                                                                   @PathVariable Long contractId,
                                                                                   @RequestPart(value = "file") MultipartFile multipartFile) throws IOException {

        Long userId = Long.parseLong(principal.getName());

        String fileUrl = s3Service.saveFile(multipartFile);

        PostContractPdfResponse response = contractHistoryService.postContractPdf(contractId, fileUrl);

        return BasicResponse.onSuccess(response);
    }


    @GetMapping("/docs")
    @Operation(summary = "계약서 목록 조회", description = "계약서 목록을 조회합니다.")
    public BasicResponse<GetContractDocumentListResponse.contractDocumentList> getContractDocumentList(){

        GetContractDocumentListResponse.contractDocumentList contractList = contractDocumentService.getContractDocumentList();

        return BasicResponse.onSuccess(contractList);
    }

    @GetMapping("/docs/{docId}")
    @Operation(summary = "계약서 상세 조회", description = "계약서를 상세 조회합니다.")
    public BasicResponse<GetContractDocumentInfoResponse> getContractDocumentInfo(@PathVariable Long docId){

        GetContractDocumentInfoResponse contractDocumentInfo = contractDocumentService.getContractDocumentInfo(docId);

        log.info("계약서 상세 조회: docId={}", docId);

        return BasicResponse.onSuccess(contractDocumentInfo);
    }
}

