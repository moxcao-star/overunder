package com.example.overunder.gamerunner;

import com.example.overunder.model.CountSec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Slf4j
@RequiredArgsConstructor
final class Timer {
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final ApplicationEventPublisher publisher;

    @Scheduled(cron = "0/1 * * * * *")
    public void fireJob() {
        if (!running.compareAndSet(false, true)) {
            return;
        }
        try {
            publisher.publishEvent(new CountSec());
        } finally {
            running.set(false);
        }

    }
}
