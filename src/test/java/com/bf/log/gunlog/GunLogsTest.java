package com.bf.log.gunlog;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.logging.Level;
import java.util.regex.Pattern;

class GunLogsTest {

    @Test
    void grep() {
        String keyword = "interesting";
        GunLog firstMatchingLog = GunLog.of(Instant.EPOCH, Level.INFO, "This is " + keyword + " log.");
        GunLog secondMatchingLog = GunLog.of(Instant.EPOCH, Level.INFO, "This is another " + keyword + " log.");
        GunLogs.of(Flux.just(
                firstMatchingLog,
                GunLog.of(Instant.EPOCH, Level.INFO, "This is boring log."),
                GunLog.of(Instant.EPOCH, Level.INFO, "This is another boring log."),
                secondMatchingLog
        ))
                .grep(Pattern.compile(keyword))
                .flux()
                .as(StepVerifier::create)
                .expectNext(firstMatchingLog)
                .expectNext(secondMatchingLog)
                .verifyComplete();
    }

    @Test
    void between() {
        GunLogs.of(
                Flux.just(
                        logAt(Instant.EPOCH.plusMillis(10)),
                        logAt(Instant.EPOCH.plusMillis(11)),
                        logAt(Instant.EPOCH.plusMillis(12)),
                        logAt(Instant.EPOCH.plusMillis(13)),
                        logAt(Instant.EPOCH.plusMillis(14)),
                        logAt(Instant.EPOCH.plusMillis(15))
                )
        )
                .between(Instant.EPOCH.plusMillis(11), Instant.EPOCH.plusMillis(14))
                .flux()
                .as(StepVerifier::create)
                .expectNext(logAt(Instant.EPOCH.plusMillis(12)))
                .expectNext(logAt(Instant.EPOCH.plusMillis(13)))
                .verifyComplete();
    }

    @Test
    void betweenInclusive() {
        GunLogs.of(
                Flux.just(
                        logAt(Instant.EPOCH.plusMillis(10)),
                        logAt(Instant.EPOCH.plusMillis(11)),
                        logAt(Instant.EPOCH.plusMillis(12)),
                        logAt(Instant.EPOCH.plusMillis(13)),
                        logAt(Instant.EPOCH.plusMillis(14)),
                        logAt(Instant.EPOCH.plusMillis(15))
                )
        )
                .betweenInclusive(Instant.EPOCH.plusMillis(11), Instant.EPOCH.plusMillis(14))
                .flux()
                .as(StepVerifier::create)
                .expectNext(logAt(Instant.EPOCH.plusMillis(11)))
                .expectNext(logAt(Instant.EPOCH.plusMillis(12)))
                .expectNext(logAt(Instant.EPOCH.plusMillis(13)))
                .expectNext(logAt(Instant.EPOCH.plusMillis(14)))
                .verifyComplete();
    }

    private GunLog logAt(Instant time) {
        return GunLog.of(time, Level.INFO, "any");
    }
}