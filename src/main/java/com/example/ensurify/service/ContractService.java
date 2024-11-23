package com.example.ensurify.service;

import com.example.ensurify.domain.User;
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
    private final UserService userService;
    private final ClientService clientService;
    private final ContractDocumentService contractDocumentService;

    /**
     * 계약 생성
     */
    @Transactional
    public CreateContractResponse createContract(Long clerkId, CreateContractRequest request) {

        User user = userService.findById(clerkId);
        Client client = clientService.findById(request.getClientId());
        ContractDocument document = contractDocumentService.findById(request.getContractDocumentId());

        Contract newContract = Contract.builder()
                .contractDocument(document)
                .user(user)
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

        User user = userService.findById(clerkId);
        List<Contract> contracts = user.getContractList();

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
