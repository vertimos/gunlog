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
}