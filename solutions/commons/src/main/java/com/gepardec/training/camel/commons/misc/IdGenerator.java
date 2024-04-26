package com.gepardec.training.camel.commons.misc;

import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@ApplicationScoped
@Named("idGenerator")
public class IdGenerator {
    public UUID nextId() {
        return UUID.randomUUID();
    }
}
