package com.example.inssurify.service;

import com.example.inssurify.common.apiPayload.code.status.ErrorStatus;
import com.example.inssurify.common.apiPayload.exception.GeneralException;
import com.example.inssurify.domain.Clerk;
import com.example.inssurify.domain.Client;
import com.example.inssurify.dto.response.GetClerkInfoResponse;
import com.example.inssurify.dto.response.GetClientInfoResponse;
import com.example.inssurify.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    /**
     * 행원 정보 조회
     */
    public GetClientInfoResponse getClientInfo(Long clientId) {

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CLIENT_NOT_FOUND));

        String gender = client.isMale() ? "남" : "여";

        return GetClientInfoResponse.builder()
                .name(client.getName())
                .email(client.getEmail())
                .age(client.getAge())
                .gender(gender)
                .build();
    }

}
