package com.gepardec.training.camel.best;

import com.gepardec.training.camel.commons.endpoint.CamelEndpoint;

public final class Endpoints {
    public static final CamelEndpoint ENTRY_DIRECT_ENDPOINT = new CamelEndpoint("direct:best_entry", "best_entry");
    public static final CamelEndpoint SPLITTER_ENTRY_SEDA_ENDPOINT = new CamelEndpoint("seda:best_splitter_entry", "best_splitter_entry");
}
