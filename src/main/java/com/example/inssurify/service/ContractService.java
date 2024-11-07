package com.example.inssurify.service;

import com.example.inssurify.domain.Clerk;
import com.example.inssurify.domain.Client;
import com.example.inssurify.domain.Contract;
import com.example.inssurify.domain.ContractDocument;
import com.example.inssurify.dto.request.CreateContractRequest;
import com.example.inssurify.dto.response.CreateContractResponse;
import com.example.inssurify.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
