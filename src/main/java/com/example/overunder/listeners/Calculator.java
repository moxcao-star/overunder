package com.example.overunder.listeners;

import com.example.overunder.model.event.BetResultEvent;
import com.example.overunder.model.Game;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class Calculator {
    private final ApplicationEventPublisher publisher;
    @EventListener
    void calculate(final Game game) {
        log.info("Calculating game {}", game.getGameId());
        BetResultEvent betResultEvent = BetResultEvent.calculateResult(game);
        if(betResultEvent.getBetDetails().isEmpty()) {
            return;
        }
        log.info("publish to log ={}", betResultEvent);
        publisher.publishEvent(betResultEvent);
    }


}
