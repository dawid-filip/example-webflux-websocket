package com.pl.df;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class WebsocketRoute {

    private final WebsocketHandler websocketHandler;

    @Bean
    WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

    @Bean
    HandlerMapping route() {
        return new SimpleUrlHandlerMapping(
                Map.of("/ws/websockets", this.websocketHandler.handler()), 5
        );
    }

}
