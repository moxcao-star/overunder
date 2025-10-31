package com.example.overunder.model.event;

import com.example.overunder.model.Side;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class PlaceBetEvent implements Serializable {
    private String gameId;
    private String sessionId;
    private String userId;
    private Long amount;
    private Side side;
}
