package com.bf.log.api;

import com.bf.log.gunlog.GunLog;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.regex.Pattern;

public interface Logs {

    Flux<GunLog> flux();

    Logs level(String level);

    default Logs debug() {
        return level("debug");
    }

    default Logs info() {
        return level("info");
    }

    default Logs warn() {
        return level("warn");
    }

    default Logs error() {
        return level("error");
    }

    Logs between(Instant start, Instant end);

    Logs betweenInclusive(Instant start, Instant end);

    default Logs after(Instant start) {
        return between(start, Instant.MAX);
    }

    default Logs before(Instant end) {
        return between(Instant.MIN, end);
    }

    Logs grep(Pattern regex);

    default Logs grep(String pattern) {
        return grep(Pattern.compile(pattern));
    }

    Logs enrich(Pattern pattern, String firstVarName, String... otherVarNames);
}
