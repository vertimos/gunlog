package com.bf.log.gunlog;

import java.time.Instant;

public class TestLog {
    private TestLog() {
    }

    public static GunLog logMessage(String message) {
        return GunLog.of(Instant.EPOCH, "info", message);
    }

    public static GunLog logLevel(String level) {
        return GunLog.of(Instant.EPOCH, level, "any");
    }

    public static GunLog logTime(Instant time) {
        return GunLog.of(time, "info", "any");
    }
}
