package com.example.inssurify.service;

import com.example.inssurify.common.apiPayload.code.status.ErrorStatus;
import com.example.inssurify.common.apiPayload.exception.GeneralException;
import com.example.inssurify.domain.Clerk;
import com.example.inssurify.domain.ContractDocument;
import com.example.inssurify.domain.ContractKeyword;
import com.example.inssurify.dto.response.GetContractDocumentInfoResponse;
import com.example.inssurify.dto.response.GetContractDocumentListResponse;
import com.example.inssurify.repository.ContractDocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ContractDocumentService {

    private final ClerkService clerkService;
    private final ContractDocumentRepository contractDocumentRepository;

    /**
     * 계약서 목록 조회
     */
    public GetContractDocumentListResponse.contractDocumentList getContractDocumentList(Long clerkId) {

        Clerk clerk = clerkService.findById(clerkId);
        List<ContractDocument> contractDocuments = clerk.getBank().getContractDocumentList();

        log.info("계약서 목록 조회: contractsNum={}", contractDocuments.size());

        List<GetContractDocumentListResponse.contractDocumentInfo> contractDocumentInfos = contractDocuments.stream()
                .map(contractDocument -> {
                    return GetContractDocumentListResponse.contractDocumentInfo.builder()
                            .contractDocumentId(contractDocument.getId())
                            .name(contractDocument.getName())
                            .category(contractDocument.getCategory())
                            .build();
                }).toList();

        return GetContractDocumentListResponse.contractDocumentList.builder()
                .contractDocumentList(contractDocumentInfos)
                .build();
    }

    /**
     * 계약서 상세 조회
     */
    public GetContractDocumentInfoResponse getContractDocumentInfo(Long contractDocumentId) {

        ContractDocument contractDocument = contractDocumentRepository.findById(contractDocumentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CONTRACT_DOCUMENT__NOT_FOUND));

        List<String> keywords = contractDocument.getKeywordList().stream()
                .map(ContractKeyword::getKeyword).toList();

        return GetContractDocumentInfoResponse.builder()
                .name(contractDocument.getName())
                .category(contractDocument.getCategory())
                .pdfUrl(contractDocument.getPdfUrl())
                .keywords(keywords)
                .build();
    }

}
