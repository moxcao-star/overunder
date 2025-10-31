package com.example.overunder.gamerunner;

import com.example.overunder.model.*;
import com.example.overunder.model.event.EndSessionEvent;
import com.example.overunder.model.event.RollEvent;
import com.example.overunder.model.event.StartSessionEvent;
import com.example.overunder.service.RollingService;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@Component
final class Dealer {
    private final Map<String, Game> gameBoard;
    private final RollingService rollingService;
    private final ApplicationEventPublisher eventPublisher;

    @EventListener
    void execute(CountSec countSec) {
        gameBoard.forEach((gameId, game) -> {
            switch (game.getCurrentStatus()) {
                case BETTING -> handleBetting(game);
                case CALCULATING -> handleCalculating(game);
                case ROLLING -> handleRolling(game);
                case PREPARE -> handlePrepare(game);
            }
        });
    }

    private void handlePrepare(Game game) {
        Game.cleanGame(game);
        game.setCurrentStatus(Status.BETTING);
        eventPublisher.publishEvent(StartSessionEvent.builder()
                .gameId(game.getGameId())
                .sessionId(game.getSession().get())
                .startTime(Instant.now().toString())
                .build());
    }

    private void handleBetting(Game game) {
        if (game.bettingCountdown() <= 0) {
            game.setCurrentStatus(Status.ROLLING);
            log.info("Game: [{}] is in ROLLING time", game.getGameId());
        }
    }

    private void handleRolling(Game game) {
        if (game.rollingCountdown() <= 0) {
            game.setCurrentStatus(Status.CALCULATING);
            log.info("Game: [{}] is in CALCULATING time", game.getGameId());
            return;
        }
        if (game.getRollingResult() != Side.UNKNOWN) {
            return;
        }
        Side rollingResult = rollingService.roll();
        eventPublisher.publishEvent(RollEvent
                .builder()
                .gameId(game.getGameId())
                .sessionId(String.valueOf(game.getSession().get()))
                .result(rollingResult)
                .rollingTime(Instant.now().toString())
                .build());
        game.setRollingResult(rollingResult);

    }

    private void handleCalculating(Game game) {
        if (game.calculatingCountdown() <= 0) {
            Game copiedGame = Game.copyFrom(game);
            eventPublisher.publishEvent(copiedGame);
            eventPublisher.publishEvent(EndSessionEvent.builder()
                    .gameId(game.getGameId())
                    .sessionId(game.getSession().get())
                    .endTime(Instant.now().toString())
                    .build());

            log.info("Game:[{}] is in PREPARE time", game.getGameId());
            game.setCurrentStatus(Status.PREPARE);
        }
    }
}
