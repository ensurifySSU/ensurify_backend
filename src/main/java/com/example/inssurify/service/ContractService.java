package com.example.inssurify.service;

import com.example.inssurify.common.apiPayload.code.status.ErrorStatus;
import com.example.inssurify.common.apiPayload.exception.GeneralException;
import com.example.inssurify.domain.Clerk;
import com.example.inssurify.domain.Contract;
import com.example.inssurify.domain.ContractKeyword;
import com.example.inssurify.dto.response.GetContractDetailsResponse;
import com.example.inssurify.dto.response.GetContractListResponse;
import com.example.inssurify.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ContractService {

    private final ClerkService clerkService;
    private final ContractRepository contractRepository;

    /**
     * 계약서 목록 조회
     */
    public GetContractListResponse.contractList getContractList(Long clerkId) {

        Clerk clerk = clerkService.findById(clerkId);
        List<Contract> contracts = clerk.getBank().getContractList();

        log.info("계약서 목록 조회: contractsNum={}", contracts.size());

        List<GetContractListResponse.contractInfo> contractInfos = contracts.stream()
                .map(contract -> {
                    return GetContractListResponse.contractInfo.builder()
                            .contractId(contract.getId())
                            .name(contract.getName())
                            .category(contract.getCategory())
                            .build();
                }).toList();

        return GetContractListResponse.contractList.builder()
                .contractList(contractInfos)
                .build();
    }

    /**
     * 계약서 상세 조회
     */
    public GetContractDetailsResponse getContractDetails(Long contractId) {

        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CONTRACT_NOT_FOUND));

        List<String> keywords = contract.getKeywordList().stream()
                .map(ContractKeyword::getKeyword).toList();

        return GetContractDetailsResponse.builder()
                .name(contract.getName())
                .category(contract.getCategory())
                .pdfUrl(contract.getPdfUrl())
                .keywords(keywords)
                .build();
    }

}
