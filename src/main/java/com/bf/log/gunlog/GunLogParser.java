package com.bf.log.gunlog;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bf.log.api.Log;
import com.bf.log.api.LogParser;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import reactor.core.publisher.Flux;

@AllArgsConstructor(staticName = "of")
public class GunLogParser implements LogParser {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @NonNull
    private final Charset charset;
    @NonNull
    private final Pattern pattern;
    @NonNull
    private final BiFunction<MatchResult, String, Log> parser;

    public static GunLogParser common() {
        return GunLogParser.of(StandardCharsets.UTF_8, Pattern.compile(
                "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}(\\.\\d{1,3})?) \\[.*\\] (DEBUG|INFO|WARN|ERROR) .*"),
                (r, s) -> Log.of(LocalDateTime.parse(r.group(1), DATE_FORMATTER).toInstant(ZoneOffset.UTC),
                        Level.parse(r.group(3)), r.group()));
    }

    @Override
    public Flux<Log> parse(InputStream source) {
        return Flux.create(emitter -> {
            try (Scanner scanner = new Scanner(source, charset)) {
                while (scanner.hasNext()) {
                    try {
                        String occurrence = scanner.findWithinHorizon(pattern, 0);
                        emitter.next(parser.apply(scanner.match(), occurrence));
                    } catch (IllegalStateException ex) {
                        // DO NOTHING
                    }
                }
                emitter.complete();
            }
        });
    }
}