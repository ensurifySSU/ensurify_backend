package com.example.ensurify.service;

import com.example.ensurify.common.apiPayload.code.status.ErrorStatus;
import com.example.ensurify.common.apiPayload.exception.GeneralException;
import com.example.ensurify.domain.Client;
import com.example.ensurify.dto.response.GetClientInfoResponse;
import com.example.ensurify.dto.response.GetClientListResponse;
import com.example.ensurify.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    /**
     * 고객 정보 조회
     */
    public GetClientInfoResponse getClientInfo(Long clientId) {

        Client client = findById(clientId);

        String gender = client.isMale() ? "남" : "여";

        return GetClientInfoResponse.builder()
                .name(client.getName())
                .email(client.getEmail())
                .age(client.getAge())
                .gender(gender)
                .build();
    }

    /**
     * 고객 목록 조회
     */
    public GetClientListResponse.clientList getClientList() {

        List<Client> clients = clientRepository.findAll();

        log.info("고객 목록 조회: clientsNum={}", clients.size());

        // 개별 조회로 인한 N+1 문제 발생 -> 관련 학습 후 보완 필요
        List<GetClientListResponse.clientInfo> clientInfos = clients.stream()
                .map(client -> {
                    return GetClientListResponse.clientInfo.builder()
                            .clientId(client.getId())
                            .name(client.getName())
                            .email(client.getEmail())
                            .build();
                }).toList();

        return GetClientListResponse.clientList.builder()
                .clientList(clientInfos)
                .build();
    }

    // id로 고객 검색
    public Client findById(Long memberId) {
        return clientRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CLIENT_NOT_FOUND));
    }
}
