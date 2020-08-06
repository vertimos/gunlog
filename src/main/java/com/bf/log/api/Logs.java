package com.bf.log.api;

import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public interface Logs {

    Flux<Log> flux();

    Logs between(Instant start, Instant end);

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

    Logs enrich(Consumer<MutableContextLog> logConsumer);

    Logs filter(Predicate<ContextLog> predicate);

    Logs join(Function<List<ContextLog>, MutableContextLog> joinFunction, Pattern firstPattern,
              Pattern... otherPatterns);

    Logs first(Pattern firstPattern, Pattern... otherPatterns);

    Logs last(Pattern firstPattern, Pattern... otherPatterns);
}
