package com.example.ensurify.common.jwt;

import com.example.ensurify.common.apiPayload.code.status.ErrorStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    public final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 요청 헤더의 Authorization 키의 값 조회
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);

        // 가져온 값에서 접두사 제거
        String token = getAccessToken(authorizationHeader);

        // 토큰이 없다면 다음 필터로 넘김(권한없는 요청일 수도 있으니)
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 유효성 검증
            tokenProvider.validateToken(token);

            // 유효한 토큰인 경우 인증 정보 설정
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (ExpiredJwtException e) {
            // 만료된 토큰 처리
            sendErrorResponse(request, response, ErrorStatus.TOKEN_EXPIRED);
            return;
        } catch (Exception e) {
            // 유효하지 않은 토큰 처리
            sendErrorResponse(request, response, ErrorStatus.TOKEN_INVALID);
            return;
        }

        filterChain.doFilter(request, response);
    }

    public static String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    private void sendErrorResponse(HttpServletRequest request, HttpServletResponse response, ErrorStatus errorStatus) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorStatus.getHttpStatus().value());

        // JSON 객체 생성
        Map<String, Object> errorResponse = new LinkedHashMap<>();
        errorResponse.put("isSuccess", "false");
        errorResponse.put("code", errorStatus.getCode());
        errorResponse.put("message", errorStatus.getMessage());

        // JSON 변환기 생성
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        // 응답 작성
        PrintWriter writer = response.getWriter();
        writer.print(jsonResponse);
        writer.flush();
    }
}

