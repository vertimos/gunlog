package com.bf.log.api;

import java.io.InputStream;

import com.bf.log.gunlog.GunLog;
import reactor.core.publisher.Flux;

public interface LogParser {
    Flux<GunLog> parse(InputStream source);
}
