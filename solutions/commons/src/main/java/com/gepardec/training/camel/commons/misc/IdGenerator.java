package com.gepardec.training.camel.commons.misc;

import java.util.UUID;

public class IdGenerator {
    public UUID nextId() {
        return UUID.randomUUID();
    }
}
