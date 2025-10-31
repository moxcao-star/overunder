package com.example.overunder.listeners.writer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;

import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
abstract class Writer<T extends Serializable> {
    protected final ObjectMapper mapper;
    protected Path LOG_PATH = getLogPath();

    Writer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @EventListener
    protected void write(T result) {
        try {
            writeLog(toJson(result));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract String toJson(T result) throws JsonProcessingException;


    protected abstract Path getLogPath();

    protected void writeLog(String json) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String line = String.format("\"[%s]\" %s%n", timestamp, json);

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
            ByteBuffer buffer = ByteBuffer.wrap(line.getBytes(StandardCharsets.UTF_8));
            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        log.info("Events written to {}", LOG_PATH);
    }
}
