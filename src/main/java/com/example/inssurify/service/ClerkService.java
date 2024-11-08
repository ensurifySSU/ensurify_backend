package com.example.inssurify.service;

import com.example.inssurify.common.apiPayload.code.status.ErrorStatus;
import com.example.inssurify.common.apiPayload.exception.GeneralException;
import com.example.inssurify.domain.Clerk;
import com.example.inssurify.dto.response.GetClerkInfoResponse;
import com.example.inssurify.repository.ClerkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClerkService {

    private final ClerkRepository clerkRepository;

    /**
     * 행원 정보 조회
     */
    public GetClerkInfoResponse getClerkInfo(Long clerkId) {

        Clerk clerk = findById(clerkId);

        return GetClerkInfoResponse.builder()
                .name(clerk.getName())
                .bank(clerk.getBank().getName())
                .imageUrl(clerk.getImageUrl())
                .build();
    }

    // id로 행원 검색
    public Clerk findById(Long memberId) {
        return clerkRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CLERK_NOT_FOUND));
    }
}
