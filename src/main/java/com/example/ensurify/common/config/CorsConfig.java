package com.example.ensurify.common.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;;

import java.util.ArrayList;
import java.util.Collections;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CorsConfig {

    public static CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        //리소스를 허용할 URL 지정
        ArrayList<String> allowedOriginPatterns = new ArrayList<>();
        allowedOriginPatterns.add("http://localhost:3000");
        allowedOriginPatterns.add("http://localhost:5173");
        allowedOriginPatterns.add("http://localhost:8080");
        allowedOriginPatterns.add("https://ensurify.store");
        allowedOriginPatterns.add("ws://localhost:8080");
        allowedOriginPatterns.add("wss://ensurify.store");
        allowedOriginPatterns.add("https://jiangxy.github.io");
        allowedOriginPatterns.add("http://192.168.0.77:5173");
        allowedOriginPatterns.add("https://ensurify.vercel.app");
        allowedOriginPatterns.add("https://enssurify-bucket.s3.ap-northeast-2.amazonaws.com");
        configuration.setAllowedOrigins(allowedOriginPatterns);

        //허용하는 HTTP METHOD 지정
        ArrayList<String> allowedHttpMethods = new ArrayList<>();
        allowedHttpMethods.add("GET");
        allowedHttpMethods.add("POST");
        allowedHttpMethods.add("PUT");
        allowedHttpMethods.add("DELETE");
        configuration.setAllowedMethods(allowedHttpMethods);

        configuration.setAllowedHeaders(Collections.singletonList("*"));
//        configuration.setAllowedHeaders(List.of(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE));

        //인증, 인가를 위한 credentials 를 TRUE로 설정
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}