package com.bf.log.api;

import java.io.InputStream;

import reactor.core.publisher.Flux;

public interface LogParser {
    Flux<Log> parse(InputStream source);
}
