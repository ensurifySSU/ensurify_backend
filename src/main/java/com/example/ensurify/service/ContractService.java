package com.example.ensurify.service;

import com.example.ensurify.domain.Clerk;
import com.example.ensurify.domain.Client;
import com.example.ensurify.domain.Contract;
import com.example.ensurify.domain.ContractDocument;
import com.example.ensurify.dto.request.CreateContractRequest;
import com.example.ensurify.dto.response.CreateContractResponse;
import com.example.ensurify.dto.response.GetContractListResponse;
import com.example.ensurify.repository.ContractRepository;
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
public class ContractService {

    private final ContractRepository contractRepository;
    private final ClerkService clerkService;
    private final ClientService clientService;
    private final ContractDocumentService contractDocumentService;

    /**
     * 계약 생성
     */
    @Transactional
    public CreateContractResponse createContract(Long clerkId, CreateContractRequest request) {

        Clerk clerk = clerkService.findById(clerkId);
        Client client = clientService.findById(request.getClientId());
        ContractDocument document = contractDocumentService.findById(request.getContractDocumentId());

        Contract newContract = Contract.builder()
                .contractDocument(document)
                .clerk(clerk)
                .client(client)
                .build();

        contractRepository.save(newContract);

        return CreateContractResponse.builder()
                .contractId(newContract.getId())
                .build();
    }

    /**
     * 계약 목록 조회
     */
    public GetContractListResponse.contractList getContractList(Long clerkId, Long docId) {

        Clerk clerk = clerkService.findById(clerkId);
        List<Contract> contracts = clerk.getContractList();

        log.info("계약 목록 조회: contractsNum={}", contracts.size());

        List<GetContractListResponse.contractInfo> contractInfos = contracts.stream()
                .filter(contract -> docId == null || contract.getContractDocument().getId().equals(docId))
                .map(contract -> {
                    ContractDocument document = contract.getContractDocument();

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    String formattedDate = contract.getCreatedAt().format(formatter);

                    return GetContractListResponse.contractInfo.builder()
                            .contractId(contract.getId())
                            .name(document.getName())
                            .category(document.getCategory())
                            .client(contract.getClient().getName())
                            .date(formattedDate)
                            .build();
                }).toList();

        return GetContractListResponse.contractList.builder()
                .contractList(contractInfos)
                .build();
    }
}
