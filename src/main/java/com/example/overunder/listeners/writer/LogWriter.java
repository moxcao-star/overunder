package com.example.overunder.listeners.writer;

import com.example.overunder.model.event.BetResultEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
class LogWriter extends Writer<BetResultEvent> {

    public LogWriter(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    protected String toJson(BetResultEvent result) throws JsonProcessingException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
    }


    @Override
    protected Path getLogPath() {
        return Paths.get("./logs/bet_results.jsonl");
    }


}
