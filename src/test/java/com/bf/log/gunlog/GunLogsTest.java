package com.bf.log.gunlog;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.regex.Pattern;

import static com.bf.log.gunlog.TestLog.*;

class GunLogsTest {

    @Test
    void grep() {
        GunLogs.of(Flux.just(
                logMessage("This is interesting log."),
                logMessage("This is boring log."),
                logMessage("This is another boring log."),
                logMessage("This is another interesting log.")
        ))
                .grep(Pattern.compile("interesting"))
                .flux()
                .as(StepVerifier::create)
                .expectNext(logMessage("This is interesting log."))
                .expectNext(logMessage("This is another interesting log."))
                .verifyComplete();
    }

    @Test
    void level() {
        GunLogs.of(
                Flux.just(
                        logLevel("info"),
                        logLevel("warning"),
                        logLevel("error"),
                        logLevel("ERROR"),
                        logLevel("INFO")
                )
        )
                .level("info")
                .flux()
                .as(StepVerifier::create)
                .expectNext(logLevel("info"))
                .expectNext(logLevel("INFO"))
                .verifyComplete();
    }

    @Test
    void between() {
        GunLogs.of(
                Flux.just(
                        logTime(Instant.EPOCH.plusMillis(10)),
                        logTime(Instant.EPOCH.plusMillis(11)),
                        logTime(Instant.EPOCH.plusMillis(12)),
                        logTime(Instant.EPOCH.plusMillis(13)),
                        logTime(Instant.EPOCH.plusMillis(14)),
                        logTime(Instant.EPOCH.plusMillis(15))
                )
        )
                .between(Instant.EPOCH.plusMillis(11), Instant.EPOCH.plusMillis(14))
                .flux()
                .as(StepVerifier::create)
                .expectNext(logTime(Instant.EPOCH.plusMillis(12)))
                .expectNext(logTime(Instant.EPOCH.plusMillis(13)))
                .verifyComplete();
    }

    @Test
    void betweenInclusive() {
        GunLogs.of(
                Flux.just(
                        logTime(Instant.EPOCH.plusMillis(10)),
                        logTime(Instant.EPOCH.plusMillis(11)),
                        logTime(Instant.EPOCH.plusMillis(12)),
                        logTime(Instant.EPOCH.plusMillis(13)),
                        logTime(Instant.EPOCH.plusMillis(14)),
                        logTime(Instant.EPOCH.plusMillis(15))
                )
        )
                .betweenInclusive(Instant.EPOCH.plusMillis(11), Instant.EPOCH.plusMillis(14))
                .flux()
                .as(StepVerifier::create)
                .expectNext(logTime(Instant.EPOCH.plusMillis(11)))
                .expectNext(logTime(Instant.EPOCH.plusMillis(12)))
                .expectNext(logTime(Instant.EPOCH.plusMillis(13)))
                .expectNext(logTime(Instant.EPOCH.plusMillis(14)))
                .verifyComplete();
    }

    @Test
    void enrich() {
        GunLogs.of(
                Flux.just(
                        logMessage("do not enrich(abc, def)"),
                        logMessage("time pair(abc, def)"),
                        logMessage("skip enriching")
                )
        )
                .enrich(Pattern.compile("pair\\(([a-z]+), ([a-z]+)\\)"), "first", "second")
                .flux()
                .as(StepVerifier::create)
                .expectNext(logMessage("do not enrich(abc, def)"))
                .expectNext(logMessage("time pair(abc, def)").put("first", "abc").put("second", "def"))
                .expectNext(logMessage("skip enriching"))
                .verifyComplete();
    }
}