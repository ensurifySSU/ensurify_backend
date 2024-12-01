package com.example.ensurify.service;

import com.example.ensurify.common.apiPayload.code.status.ErrorStatus;
import com.example.ensurify.common.apiPayload.exception.GeneralException;
import com.example.ensurify.domain.User;
import com.example.ensurify.domain.Client;
import com.example.ensurify.domain.ContractHistory;
import com.example.ensurify.domain.ContractDocument;
import com.example.ensurify.dto.request.CreateContractRequest;
import com.example.ensurify.dto.response.CreateContractResponse;
import com.example.ensurify.dto.response.GetContractListResponse;
import com.example.ensurify.dto.response.PostContractPdfResponse;
import com.example.ensurify.repository.ContractHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ContractHistoryService {

    private final ContractHistoryRepository contractHistoryRepository;
    private final UserService userService;
    private final ClientService clientService;
    private final ContractDocumentService contractDocumentService;

    /**
     * 계약 생성
     */
    @Transactional
    public CreateContractResponse createContract(Long clerkId, CreateContractRequest request, Long roomId) {

        User user = userService.findById(clerkId);
        Client client = clientService.findById(request.getClientId());
        ContractDocument document = contractDocumentService.findById(request.getContractDocumentId());

        ContractHistory newContractHistory = ContractHistory.builder()
                .contractDocument(document)
                .user(user)
                .client(client)
                .build();

        contractHistoryRepository.save(newContractHistory);

        return CreateContractResponse.builder()
                .contractId(newContractHistory.getId())
                .roomId(roomId)
                .build();
    }

    /**
     * 계약 내역 목록 조회
     */
    public GetContractListResponse.contractList getContractList(Long clerkId, Long docId) {

        User user = userService.findById(clerkId);
        List<ContractHistory> contractHistories = user.getContractHistoryList();

        log.info("계약 내역 목록 조회: contractHistoryNum={}", contractHistories.size());

        List<GetContractListResponse.contractInfo> contractInfos = contractHistories.stream()
                .filter(contractHistory -> docId == null || contractHistory.getContractDocument().getId().equals(docId))
                .map(contractHistory -> {
                    ContractDocument document = contractHistory.getContractDocument();

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    String formattedDate = contractHistory.getCreatedAt().format(formatter);

                    return GetContractListResponse.contractInfo.builder()
                            .contractId(contractHistory.getId())
                            .name(document.getName())
                            .category(document.getCategory())
                            .client(contractHistory.getClient().getName())
                            .date(formattedDate)
                            .pdfUrl(contractHistory.getPdfUrl())
                            .build();
                }).toList();

        return GetContractListResponse.contractList.builder()
                .contractList(contractInfos)
                .build();
    }

    /**
     * 계약 내역 PDF 저장
     */
    @Transactional
    public PostContractPdfResponse postContractPdf(Long contractId, String pdfUrl) {

        ContractHistory contractHistory = findById(contractId);

        contractHistory.setPdfUrl(pdfUrl);

        return PostContractPdfResponse.builder()
                .pdfUrl(pdfUrl)
                .build();
    }

    private ContractHistory findById(Long contractId) {
        return contractHistoryRepository.findById(contractId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CONTRACT_HISTORY_NOT_FOUND));
    }
}
