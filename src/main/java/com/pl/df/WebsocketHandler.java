package com.pl.df;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

import java.time.Duration;

@Configuration
@Log4j2
public class WebsocketHandler {

    private Flux<String> produceMessages() {
        return Flux.range(1, 20)
                .map(i -> "Producing message " + i)
                .delayElements(Duration.ofMillis(50));
    }

    @Bean
    public WebSocketHandler handler() {
        return session -> {

            Flux<WebSocketMessage> serverResponse =
                    produceMessages()
                    .doOnNext(log::info)
                    .map(session::textMessage)
                    .doFinally(
                        signalType -> log.info(session.getId() + " : The outbound connection signal: " + signalType)
                    );

            Flux<String> clientRequest = session
                    .receive()
                    .map(WebSocketMessage::getPayloadAsText)
                    .doFinally(signalType -> {
                        log.info(session.getId() + " : The inbound connection signal: " + signalType);
                        if (signalType.equals(SignalType.ON_COMPLETE)) {
                            session.close().subscribe();
                        }
                    })
                    .doOnNext(log::info);

            return session.send(serverResponse)
                    .and(clientRequest);

        };
    }

}
