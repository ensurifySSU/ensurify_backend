package com.example.inssurify.service;

import com.example.inssurify.common.apiPayload.code.status.ErrorStatus;
import com.example.inssurify.common.apiPayload.exception.GeneralException;
import com.example.inssurify.common.jwt.TokenProvider;
import com.example.inssurify.domain.Clerk;
import com.example.inssurify.dto.request.LoginRequest;
import com.example.inssurify.dto.response.GetClerkInfoResponse;
import com.example.inssurify.dto.response.LoginResponse;
import com.example.inssurify.repository.ClerkRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Objects;

import static com.example.inssurify.common.apiPayload.code.status.ErrorStatus.CLERK_NOT_FOUND;
import static com.example.inssurify.common.apiPayload.code.status.ErrorStatus.INVALID_PASSWORD;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClerkService {

    private final ClerkRepository clerkRepository;
    private final TokenProvider tokenProvider;

    private static final Duration ACCESS_TOKEN_DURATION = Duration.ofMinutes(30);

    /**
     * 로그인
     */
    @Transactional
    public LoginResponse login(LoginRequest request) {

        Clerk clerk = clerkRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new GeneralException(CLERK_NOT_FOUND));

        log.info("로그인: clerkId={}", clerk.getId());

        if (!Objects.equals(request.getPassword(), clerk.getPassword())) {
            throw new GeneralException(INVALID_PASSWORD);
        }

        String accessToken = tokenProvider.generateToken(clerk, ACCESS_TOKEN_DURATION);
        log.info("access_token: " + accessToken);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .build();
    }

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
                .orElseThrow(() -> new GeneralException(CLERK_NOT_FOUND));
    }
}
