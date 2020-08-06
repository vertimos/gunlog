package com.bf.log.gunlog;

import com.bf.log.api.ContextLog;
import com.bf.log.api.Log;
import com.bf.log.api.Logs;
import com.bf.log.api.MutableContextLog;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@AllArgsConstructor(staticName = "of")
public class GunLogs implements Logs {
    private final Flux<Log> logs;

    @Override
    public Flux<Log> flux() {
        return logs;
    }

    @Override
    public Logs grep(String regex) {
        return null;
    }

    @Override
    public Logs enrich(Pattern pattern, String firstVarName, String... otherVarNames) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Logs enrich(Consumer<MutableContextLog> logConsumer) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Logs filter(Predicate<ContextLog> predicate) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Logs join(Function<List<ContextLog>, MutableContextLog> joinFunction,
                     Pattern firstPattern, Pattern... otherPatterns) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Logs first(Pattern firstPattern, Pattern... otherPatterns) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Logs last(Pattern firstPattern, Pattern... otherPatterns) {
        // TODO Auto-generated method stub
        return null;
    }
}
