package com.bf.log.gunlog;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bf.log.api.Log;
import com.bf.log.api.LogParser;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import reactor.core.publisher.Flux;

@AllArgsConstructor(staticName = "of")
public class GunLogParser implements LogParser {
    @NonNull 
    private final Charset charset;
    @NonNull 
    private final Pattern pattern;
    @NonNull
    private final BiFunction<Pattern, String, Log> parser;
    
    public static GunLogParser common() {
        return GunLogParser.of(
            StandardCharsets.UTF_8,
            Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} \\[.*\\] (DEBUG|INFO|WARN|ERROR) .*"),
            (p, s) -> {
                Matcher m = p.matcher(s);
                return Log.of(Instant.parse(m.group(1)), Level.parse(m.group(3)), m.group(0));
            }
        );
    }

    @Override
    public Flux<Log> parse(InputStream source) {
        return Flux.create(emitter -> {
            try (Scanner scanner = new Scanner(source, charset)) {
                while (scanner.hasNext(pattern)) {
                    String occurrence = scanner.next(pattern);
                    emitter.next(parser.apply(pattern, occurrence));
                }
                emitter.complete();
            }
        });
    }
}