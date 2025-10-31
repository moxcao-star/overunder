package com.example.overunder.listeners.writer;

import com.example.overunder.model.event.PlaceBetEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
final class PlaceBetWriter extends Writer<PlaceBetEvent> {

    PlaceBetWriter(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    protected boolean batchingFlush() {
        return true;
    }

    @Override
    protected String toJson(PlaceBetEvent result) throws JsonProcessingException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
    }

    @Override
    protected Path getLogPath() {
        return Paths.get("./logs/place_bet_history.jsonl");
    }
}
