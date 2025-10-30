package com.example.overunder.service;

import com.example.over_under.BetReq;
import com.example.over_under.BetRes;
import com.example.over_under.OverUnderServiceGrpc;
import com.example.overunder.model.Bet;
import com.example.overunder.model.Game;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.grpc.server.service.GrpcService;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.example.overunder.model.Status.BETTING;

@Slf4j
@GrpcService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GameService extends OverUnderServiceGrpc.OverUnderServiceImplBase {
    private final Map<String, Game> gameBoard;

    @Override
    public void placeBet(BetReq request, StreamObserver<BetRes> responseObserver) {
        log.info("Received placeBet request {}", request);
        String gameId = request.getGameId();
        String userId = request.getUserId();
        long amount = request.getAmount();
        Bet bet;
        try {
            bet = Bet.valueOf(request.getChoice());
        } catch (IllegalArgumentException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Wrong Bet type").asRuntimeException());
            return;
        }
        if (!gameBoard.containsKey(gameId)) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Game Not Found").asRuntimeException());
            return;
        }
        Game game = gameBoard.get(gameId);
        if (!game.getCurrentStatus().equals(BETTING)) {
            responseObserver.onError(Status.PERMISSION_DENIED
                    .withDescription("Not In Betting Time, Try Again In A Few Seconds")
                    .asRuntimeException());
            return;
        }
        Map<String, List<Game.BetInfo>> usersOnGame = game.getUsersOnGame();
        if (!usersOnGame.containsKey(userId)) {
            usersOnGame.put(userId, new LinkedList<>());
        }
        List<Game.BetInfo> betInfos = usersOnGame.get(userId);
        Game.BetInfo betInfo = new Game.BetInfo();
        betInfo.setBetAmount(amount);
        betInfo.setBet(bet);
        betInfos.add(betInfo);
        responseObserver.onNext(BetRes.newBuilder().setMessage("Place Bet Successfully").build());
        responseObserver.onCompleted();
    }

}
