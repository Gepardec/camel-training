package com.gepardec.training.camel.best.db;

import com.ninja_squad.dbsetup.operation.Operation;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;

public class CommonOperations {
    public static final Operation DELETE_ALL =
            deleteAllFrom("order_to_producer");
}