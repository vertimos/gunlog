package com.bf.log.gunlog;

import com.bf.log.api.ContextLog;
import com.bf.log.api.Logs;
import com.bf.log.api.MutableContextLog;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@AllArgsConstructor(staticName = "of")
public class GunLogs implements Logs {
    private final Flux<GunLog> logs;

    @Override
    public Flux<GunLog> flux() {
        return logs;
    }

    @Override
    public Logs between(Instant start, Instant end) {
        return GunLogs.of(flux().filter(log -> log.getTime().isAfter(start) && log.getTime().isBefore(end)));
    }

    @Override
    public Logs betweenInclusive(Instant start, Instant end) {
        return GunLogs.of(flux().filter(log ->
                        (log.getTime().equals(start) || log.getTime().isAfter(start))
                                && (log.getTime().equals(end) || log.getTime().isBefore(end))
                )
        );
    }

    @Override
    public Logs grep(Pattern pattern) {
        return GunLogs.of(flux().filter(log -> pattern.matcher(log.getValue()).find()));
    }

    @Override
    public Logs enrich(Pattern pattern, String firstVarName, String... otherVarNames) {
        return null;
    }

    @Override
    public Logs enrich(Consumer<MutableContextLog> logConsumer) {
        return null;
    }

    @Override
    public Logs filter(Predicate<ContextLog> predicate) {
        return null;
    }

    @Override
    public Logs join(Function<List<ContextLog>, MutableContextLog> joinFunction,
                     Pattern firstPattern, Pattern... otherPatterns) {
        return null;
    }

    @Override
    public Logs first(Pattern firstPattern, Pattern... otherPatterns) {
        return null;
    }

    @Override
    public Logs last(Pattern firstPattern, Pattern... otherPatterns) {
        return null;
    }
}
