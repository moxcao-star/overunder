package com.example.overunder.listeners.writer;

import com.example.overunder.model.event.RollEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class RollEventWriter extends Writer<RollEvent>{
    protected RollEventWriter(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    protected boolean batchingFlush() {
        return false;
    }

    @Override
    protected String toJson(RollEvent result) throws JsonProcessingException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
    }

    @Override
    protected Path getLogPath() {
        return Paths.get("./logs/roll_history.jsonl");
    }
}
