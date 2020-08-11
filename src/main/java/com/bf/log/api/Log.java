package com.bf.log.api;

import java.time.Instant;

public interface Log {
    Instant getTime();

    String getLevel();

    String getValue();
}
