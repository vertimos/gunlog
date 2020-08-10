package com.bf.log.gunlog;

import com.bf.log.api.LogParser;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor(staticName = "of", access = AccessLevel.PRIVATE)
public class GunLogParser implements LogParser {
    public static final DateTimeFormatter COMMON_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    @NonNull
    private final Charset charset;
    @NonNull
    private final Collection<GunLogDef> logDefinitions;

    public static GunLogParser timeLevelMessage(Pattern pattern) {
        return line(pattern, (r, s) -> GunLog.of(
                LocalDateTime.parse(r.group(1), COMMON_DATE_FORMATTER).toInstant(ZoneOffset.UTC),
                r.group(2), r.group()));
    }

    public static GunLogParser timeLevelMessage(Pattern pattern,
                                                DateTimeFormatter dateTimeFormatter) {
        return line(pattern, (r, s) -> GunLog.of(
                LocalDateTime.parse(r.group(1), dateTimeFormatter).toInstant(ZoneOffset.UTC),
                r.group(2), r.group()));
    }

    public static GunLogParser line(Pattern pattern,
                                    BiFunction<MatchResult, String, GunLog> logFactory) {
        return line(pattern, logFactory, StandardCharsets.UTF_8);
    }

    public static GunLogParser line(Pattern pattern,
                                    BiFunction<MatchResult, String, GunLog> logFactory, Charset charset) {
        return GunLogParser.of(charset,
                GunLogDef.of(
                        Pattern.compile(
                                pattern.pattern() + "(\\R.*?)*(?=\\R" + pattern.pattern() + ")"),
                        logFactory),
                GunLogDef.of(Pattern.compile(pattern.pattern() + "(\\R.*)*"), logFactory)
        );
    }

    public static GunLogParser of(Charset charset, GunLogDef logDef, GunLogDef... otherLogDefs) {
        return of(charset, Stream.concat(Stream.of(logDef), Stream.of(otherLogDefs))
                .collect(Collectors.toList()));
    }

    @Override
    public Flux<GunLog> parse(InputStream source) {
        return Flux.create(emitter -> {
            try (Scanner scanner = new Scanner(source, charset)) {
                logDefinitions.forEach(emitLogs(emitter, scanner));
                emitter.complete();
            }
        });
    }

    private Consumer<? super GunLogDef> emitLogs(FluxSink<GunLog> emitter, Scanner scanner) {
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
