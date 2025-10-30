package com.example.overunder.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public class Game {
    private String gameId;
    private AtomicInteger gameSession = new AtomicInteger(0);
    private Status currentStatus;
    private AtomicInteger bettingTime = new AtomicInteger(20);
    private AtomicInteger rollingTime = new AtomicInteger(3);
    private AtomicInteger calculatingTime = new AtomicInteger(2);
    private Bet rollingResult = Bet.UNKNOWN;
    private Map<String, List<BetInfo>> usersOnGame = new ConcurrentHashMap<>();

    @Getter
    @Setter
    public static class BetInfo {
        private Bet bet = Bet.UNKNOWN;
        private long betAmount = 0L;
    }

    public int bettingCountdown(){
        return bettingTime.decrementAndGet();
    }
    public int rollingCountdown(){
        return rollingTime.decrementAndGet();
    }
    public int calculatingCountdown(){
        return calculatingTime.decrementAndGet();
    }

    public static Game copyFrom(Game game) {
        Game gameCopy = new Game();
        gameCopy.gameId = game.gameId;
        gameCopy.gameSession = new AtomicInteger(game.gameSession.get());
        gameCopy.currentStatus = game.currentStatus;
        gameCopy.rollingResult = game.rollingResult;
        gameCopy.bettingTime = new AtomicInteger(game.bettingTime.get());
        gameCopy.rollingTime = new AtomicInteger(game.rollingTime.get());
        gameCopy.calculatingTime = new AtomicInteger(game.calculatingTime.get());

        Map<String, List<BetInfo>> copiedUsers = new ConcurrentHashMap<>();
        for (Map.Entry<String, List<BetInfo>> entry : game.usersOnGame.entrySet()) {
            List<BetInfo> originalList = entry.getValue();

            List<BetInfo> copiedList = originalList.stream()
                    .map(original -> {
                        BetInfo copy = new BetInfo();
                        copy.setBet(original.getBet());
                        copy.setBetAmount(original.getBetAmount());
                        return copy;
                    })
                    .toList();

            copiedUsers.put(entry.getKey(), copiedList);
        }

        gameCopy.usersOnGame = copiedUsers;

        return gameCopy;
    }
    public static void cleanGame(Game game){
        game.rollingResult = Bet.UNKNOWN;
        game.gameSession.incrementAndGet();
        game.bettingTime = new AtomicInteger(20);
        game.rollingTime = new AtomicInteger(3);
        game.calculatingTime = new AtomicInteger(2);
        game.usersOnGame.clear();
    }

    public void setBettingTime(int value) {
        this.bettingTime = new AtomicInteger(value);
    }

    public void setRollingTime(int value) {
        this.rollingTime = new AtomicInteger(value);
    }

    public void setCalculatingTime(int value) {
        this.calculatingTime = new AtomicInteger(value);
    }
}
