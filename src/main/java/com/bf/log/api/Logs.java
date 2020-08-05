package com.bf.log.api;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import reactor.core.publisher.Flux;

public interface Logs {

    Flux<Log> flux();

    Logs grep(String regex);

    Logs enrich(String regexWithVars);

    Logs enrich(BiConsumer<Log, Context> regexWithVars);

    Logs filter(Predicate<Log> predicate);

    Logs filter(BiPredicate<Log, Context> predicate);

    Logs join(Function<List<Log>, Log> joinFunction, String regex, String... regexes);

    Logs first(String regex, String... regexes);

    Logs last(String regex, String... regexes);
}
