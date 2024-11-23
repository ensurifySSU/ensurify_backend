package com.example.ensurify.service;

import com.example.ensurify.common.apiPayload.code.status.ErrorStatus;
import com.example.ensurify.common.apiPayload.exception.GeneralException;
import com.example.ensurify.domain.ContractDocument;
import com.example.ensurify.domain.ContractKeyword;
import com.example.ensurify.dto.response.GetContractDocumentInfoResponse;
import com.example.ensurify.dto.response.GetContractDocumentListResponse;
import com.example.ensurify.repository.ContractDocumentRepository;
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

    private final ContractDocumentRepository contractDocumentRepository;

    /**
     * 계약서 목록 조회
     */
    public GetContractDocumentListResponse.contractDocumentList getContractDocumentList() {

        List<ContractDocument> contractDocuments = contractDocumentRepository.findAll();

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

        ContractDocument contractDocument = findById(contractDocumentId);

        List<String> keywords = contractDocument.getKeywordList().stream()
                .map(ContractKeyword::getKeyword).toList();

        return GetContractDocumentInfoResponse.builder()
                .name(contractDocument.getName())
                .category(contractDocument.getCategory())
                .keywords(keywords)
                .build();
    }

    // id로 계약서 검색
    public ContractDocument findById(Long docId) {
        return contractDocumentRepository.findById(docId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CONTRACT_DOCUMENT_NOT_FOUND));
    }
}
