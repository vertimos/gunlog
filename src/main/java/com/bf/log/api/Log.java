package com.bf.log.api;

import java.time.Instant;
import java.util.logging.Level;

public interface Log {
    Instant getTime();

    String getLevel();

    String getValue();
}
