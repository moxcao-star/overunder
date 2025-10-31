package com.example.overunder.service;

import com.example.overunder.model.Side;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RollingService {
    Random random = new Random();
    ApplicationEventPublisher publisher;
    public Side roll() {
        int totalDice = random.nextInt(1, 7)
                + random.nextInt(1, 7)
                + random.nextInt(1, 7);
        if (totalDice < 11) return Side.UNDER;
        return Side.OVER;
    }
}
