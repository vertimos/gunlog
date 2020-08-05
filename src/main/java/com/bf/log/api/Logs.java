package com.bf.log.api;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import reactor.core.publisher.Flux;

public interface Logs {

    Flux<Log> flux();

    Logs grep(String regex);

    Logs enrich(Pattern pattern, String firstVarName, String... otherVarNames);

    Logs enrich(Consumer<MutableContextLog> logConsumer);

    Logs filter(Predicate<ContextLog> predicate);

    Logs join(Function<List<ContextLog>, MutableContextLog> joinFunction, Pattern firstPattern,
            Pattern... otherPatterns);

    Logs first(Pattern firstPattern, Pattern... otherPatterns);

    Logs last(Pattern firstPattern, Pattern... otherPatterns);
}
