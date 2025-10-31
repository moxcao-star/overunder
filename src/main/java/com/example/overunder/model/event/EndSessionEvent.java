package com.example.overunder.model.event;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
public class EndSessionEvent implements Serializable {
    private String gameId;
    private int sessionId;
    private String endTime;
}
