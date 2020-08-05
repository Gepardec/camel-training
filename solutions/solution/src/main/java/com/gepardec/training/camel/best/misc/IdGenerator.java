package com.gepardec.training.camel.best.misc;

import java.util.UUID;

public class IdGenerator {
    public UUID nextId() {
        return UUID.randomUUID();
    }
}
