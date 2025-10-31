package com.example.overunder.model.event;

import com.example.overunder.model.Side;
import com.example.overunder.model.Game;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Builder
@Data
public class BetResultEvent implements Serializable {
    private String gameId;
    private String gameSessionId;
    private Map<String, List<BetDetail>> betDetails;

    public static BetResultEvent calculateResult(Game game) {
        Map<String, List<Game.BetInfo>> usersOnGame = game.getUsersOnGame();
        Map<String, List<BetDetail>> betDetails = new HashMap<>();
        usersOnGame.forEach((userId, bets) -> {
            List<BetDetail> details = bets.stream().map(betInfo -> {
                BetDetail detail = new BetDetail();
                if (game.getRollingResult() == betInfo.getSide()) {
                    detail.setChangeAmount(betInfo.getBetAmount());
                } else {
                    detail.setChangeAmount(-betInfo.getBetAmount());
                }
                detail.setSide(betInfo.getSide());
                detail.setBetAmount(betInfo.getBetAmount());
                return detail;
            }).toList();
            betDetails.put(userId, details);
        });
        return BetResultEvent.builder()
                .gameId(game.getGameId())
                .gameSessionId(game.getSession().toString())
                .betDetails(betDetails)
                .build();
    }

    @Data
    public static class BetDetail implements Serializable {
        private Side side;
        private Long betAmount;
        private Long changeAmount;
    }
}
