package com.example.overunder.service;

import com.example.overunder.model.Bet;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RollingService {
    Random random = new Random();

    public Bet roll() {
        int totalDice = random.nextInt(1, 7)
                + random.nextInt(1, 7)
                + random.nextInt(1, 7);
        if (totalDice < 11) return Bet.UNDER;
        return Bet.OVER;
    }
}
