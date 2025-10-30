package com.example.overunder.model.event;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
@Data
@Builder
public class EndSessionEvent implements Serializable {
    private String gameId;
    private int sessionId;
}
