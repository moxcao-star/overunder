package com.example.overunder.listeners.writer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
abstract class Writer<T extends Serializable> {
    @Autowired
    protected ObjectMapper mapper;
    protected final Path LOG_PATH = getLogPath();
    protected final Queue<String> queue = new ConcurrentLinkedQueue<>();

    protected final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    protected Writer(ObjectMapper mapper) {
        this.mapper = mapper;
        scheduler.scheduleAtFixedRate(this::queueConsumer, 10,10, TimeUnit.SECONDS);
    }

    private void queueConsumer() {

        if(queue.isEmpty()) {
            return;
        }
        StringBuilder line = new StringBuilder();
        while (!queue.isEmpty()) {
            line.append(queue.poll());
        }
        log.info("BATCH: {}", line);
        flush(line.toString());
    }

    @EventListener
    protected void write(final T result) {
        try {
            String json = toJson(result);
            if (batchingFlush()) {
                queue.add(json);
                return;
            }
            flush(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract boolean batchingFlush();

    protected abstract String toJson(T result) throws JsonProcessingException;

    protected abstract Path getLogPath();

    protected void flush(String json) {
        try {
            Files.createDirectories(LOG_PATH.getParent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (FileChannel channel = FileChannel.open(
                getLogPath(),
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.APPEND
        )) {
            Files.createDirectories(LOG_PATH.getParent());
            ByteBuffer buffer = ByteBuffer.wrap(json.getBytes(StandardCharsets.UTF_8));
            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("Events written to {}", LOG_PATH);
    }


}
