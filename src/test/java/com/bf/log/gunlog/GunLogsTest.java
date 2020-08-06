package com.bf.log.gunlog;

import com.bf.log.api.Log;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.logging.Level;

class GunLogsTest {

    @Test
    void grep() {
        String keyword = "interesting";
        Log firstMatchingLog = Log.of(Instant.EPOCH, Level.INFO, "This is " + keyword + " log.");
        Log secondMatchingLog = Log.of(Instant.EPOCH, Level.INFO, "This is another " + keyword + " log.");
        GunLogs.of(Flux.just(
                firstMatchingLog,
                Log.of(Instant.EPOCH, Level.INFO, "This is boring log."),
                Log.of(Instant.EPOCH, Level.INFO, "This is another boring log."),
                secondMatchingLog
        ))
                .grep(keyword)
                .flux()
                .as(StepVerifier::create)
                .expectNext(firstMatchingLog)
                .expectNext(secondMatchingLog)
                .verifyComplete();
    }
}