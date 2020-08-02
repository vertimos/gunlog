package com.bf;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.logging.Level;

import com.bf.log.api.Log;
import com.bf.log.gunlog.GunLogParser;

import org.junit.jupiter.api.Test;

import reactor.test.StepVerifier;

public class GunLogParserTest {

    @Test
    public void oneLine_success() {
        String logLine = "2020-08-01 22:30:02 [main] INFO Main.java: This is sample log.";
        GunLogParser.common()
            .parse(from(logLine))
            .as(StepVerifier::create)
            .expectNext(Log.of(Instant.parse("2020-08-01T22:30:02.000Z"), Level.INFO, logLine))
            .verifyComplete();
    
    }

    private InputStream from(String logs) {
        return new ByteArrayInputStream(logs.getBytes(StandardCharsets.UTF_8));
    }
}