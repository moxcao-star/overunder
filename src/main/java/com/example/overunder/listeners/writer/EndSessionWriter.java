package com.example.overunder.listeners.writer;

import com.example.overunder.model.event.EndSessionEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class EndSessionWriter extends Writer<EndSessionEvent> {
    EndSessionWriter(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    protected String toJson(EndSessionEvent result) throws JsonProcessingException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
    }

    @Override
    protected Path getLogPath() {
        return Paths.get("./logs/end_session_history.jsonl");
    }
}
