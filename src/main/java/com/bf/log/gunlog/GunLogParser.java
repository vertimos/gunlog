package com.bf.log.gunlog;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.regex.Pattern;

import com.bf.log.api.Log;
import com.bf.log.api.LogParser;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@AllArgsConstructor(staticName = "of")
public class GunLogParser implements LogParser {
    private static final String DEFAULT_LOG_PATTERN =
            "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}(\\.\\d{1,3})?) \\[.*\\] (DEBUG|INFO|WARN|ERROR) .*";

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @NonNull
    private final Charset charset;
    @NonNull
    private final Collection<GunLogDef> logDefinitions;

    public static GunLogParser common() {
        return GunLogParser
                .of(StandardCharsets.UTF_8, List.of(
                        GunLogDef.of(
                                Pattern.compile(DEFAULT_LOG_PATTERN + "(\\R.*?)*(?=\\R"
                                        + DEFAULT_LOG_PATTERN + ")"),
                                (r, s) -> Log.of(
                                        LocalDateTime.parse(r.group(1), DATE_FORMATTER)
                                                .toInstant(ZoneOffset.UTC),
                                        Level.parse(r.group(3)), r.group())),
                        GunLogDef.of(Pattern.compile(DEFAULT_LOG_PATTERN + "(\\R.*)*"),
                                (r, s) -> Log.of(
                                        LocalDateTime.parse(r.group(1), DATE_FORMATTER)
                                                .toInstant(ZoneOffset.UTC),
                                        Level.parse(r.group(3)), r.group()))

                ));
    }

    @Override
    public Flux<Log> parse(InputStream source) {
        return Flux.create(emitter -> {
            try (Scanner scanner = new Scanner(source, charset)) {
                logDefinitions.forEach(emitLogs(emitter, scanner));
                emitter.complete();
            }
        });
    }

    private Consumer<? super GunLogDef> emitLogs(FluxSink<Log> emitter, Scanner scanner) {
        return logDef -> {
            while (scanner.hasNext()) {
                try {
                    String occurrence = scanner.findWithinHorizon(logDef.getPattern(), 0);
                    emitter.next(logDef.getLogFactory().apply(scanner.match(), occurrence));
                } catch (IllegalStateException ex) {
                    break;
                }
            }
        };
    }
}
