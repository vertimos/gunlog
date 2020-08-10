package com.bf.log.gunlog;

import com.bf.log.api.ContextLog;
import com.bf.log.api.Logs;
import com.bf.log.api.MutableContextLog;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.List;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@AllArgsConstructor(staticName = "of")
public class GunLogs implements Logs {
    private final Flux<GunLog> logs;

    @Override
    public Flux<GunLog> flux() {
        return logs;
    }

    @Override
    public Logs level(String level) {
        return GunLogs.of(flux().filter(log -> log.getLevel().equalsIgnoreCase(level)));
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
        return GunLogs.of(flux().map(log -> {
            Matcher matcher = pattern.matcher(log.getValue());
            return matcher.find()
                    ? enrich(log, matcher, Stream.concat(Stream.of(firstVarName), Stream.of(otherVarNames)).toArray(String[]::new))
                    : log;
        }));
    }

    private GunLog enrich(GunLog log, MatchResult matchResult, String[] vars) {
        int i1 = matchResult.groupCount();
        if (i1 != vars.length) {
            throw new GunLogException("TODO: enrich");
        }
        for (int i = 0; i < vars.length; i++) {
            log.put(vars[i], matchResult.group(i + 1));
        }
        return log;
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
