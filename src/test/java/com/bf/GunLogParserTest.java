package com.bf;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.bf.log.api.Log;
import com.bf.log.gunlog.GunLogParser;

import org.junit.jupiter.api.Test;

import reactor.test.StepVerifier;

public class GunLogParserTest {

    @Test
    public void noLogs_emptySource_emptyResult() {
        GunLogParser.common().parse(asInputStream(lines())).as(StepVerifier::create).verifyComplete();
    }

    @Test
    public void noLogs_nonEmptySource_emptyResult() {
        GunLogParser.common().parse(asInputStream(lines("This is not a log"))).as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    public void oneLog_short_success() {
        String logLine = "2020-08-01 22:30:00.000 [main] INFO Main.java: This is sample log.";
        GunLogParser.common().parse(asInputStream(lines(logLine))).as(StepVerifier::create)
                .expectNext(Log.of(Instant.parse("2020-08-01T22:30:00.000Z"), Level.INFO, logLine)).verifyComplete();
    }

    @Test
    public void twoLogs_short_success() {
        String first = "2020-08-01 22:30:00.000 [main] INFO Main.java: This is sample log.";
        String second = "2020-08-01 22:30:02.000 [main] INFO Main.java: This is another log.";
        GunLogParser.common().parse(asInputStream(lines(first, second))).log().as(StepVerifier::create)
                .expectNext(Log.of(Instant.parse("2020-08-01T22:30:00.000Z"), Level.INFO, first))
                .expectNext(Log.of(Instant.parse("2020-08-01T22:30:02.000Z"), Level.INFO, second)).verifyComplete();
    }

    @Test
    public void oneLog_long_success() {
        String fullLog = lines("2020-08-01 22:30:00.000 [main] INFO Main.java: This is sample log.",
                "second line of log", "third part of log");
        GunLogParser.common().parse(asInputStream(fullLog)).as(StepVerifier::create)
                .expectNext(Log.of(Instant.parse("2020-08-01T22:30:00.000Z"), Level.INFO, fullLog)).verifyComplete();
    }

    private String lines(String... lines) {
        return Stream.of(lines).collect(Collectors.joining(System.lineSeparator()));
    }

    private InputStream asInputStream(String log) {
        return new ByteArrayInputStream(log.getBytes(StandardCharsets.UTF_8));
    }
}