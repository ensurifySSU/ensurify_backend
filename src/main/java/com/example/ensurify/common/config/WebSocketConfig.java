package com.example.ensurify.common.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub");    // 메시지 구독 요청: 메시지 송신
        registry.setApplicationDestinationPrefixes("/pub");   // 메시지 발행 요청: 메시지 수신
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp")   //SockJS 연결 주소(ws://localhost:8080/ws-stomp)
                .setAllowedOriginPatterns("*"); //CORS 허용 설정
//                .withSockJS(); //버전 낮은 브라우저에서도 적용 가능
    }
}
