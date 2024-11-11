package com.example.inssurify.common.jwt;

import com.example.inssurify.domain.Clerk;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class TokenProvider {

    @Value("${spring.jwt.secret}")
    private String secretKeyString;

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }

    public String generateToken(Clerk user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    /**
     * JWT 토큰을 생성하는 메서드이다.
     *
     * @param expiry 토큰의 만료 시간
     * @param user   회원 정보
     * @return 생성된 토큰
     */
    private String makeToken(Date expiry, Clerk user) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(String.valueOf(user.getId()))
                .claim("id", user.getId())
                .signWith(getSecretKey(), SignatureAlgorithm.HS256) // HS256 방식으로 암호화
                .compact();
    }

    /**
     * JWT 토큰의 유효성을 검증하는 메서드이다.
     *
     * @param token 검증할 JWT 토큰
     */
    public void validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            // 토큰이 만료되었음을 명시적으로 처리
            log.info("Expired JWT token: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            // 서명 오류, 잘못된 토큰 등 기타 JWT 관련 오류를 처리
            log.info("Invalid JWT token: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * JWT 토큰의 만료 시간을 조회하는 메서드이다.
     *
     * @param token 검증할 JWT 토큰
     */
    public void isExpired(String token) {
        Claims claims = getClaims(token);
        claims.getExpiration();
    }

    /**
     * 토큰에서 회원 ID를 가져오는 메서드이다.
     * @param token JWT 토큰
     * @return 회원 ID
     */
    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    /**
     * 토큰 기반으로 인증 정보를 가져오는 메서드이다.
     *
     * @param token 인증된 회원의 토큰
     * @return 인증 정보를 담은 Authentication 객체
     */
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities), token, authorities);
    }

    /**
     * 주어진 토큰에서 클레임을 조회하는 메서드이다.
     *
     * @param token JWT 토큰
     * @return 클레임 객체
     */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
