package com.bf.log.api;

import java.time.Instant;
import java.util.logging.Level;

import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "of")
public class Log {
    @NonNull
    Instant time;
    @NonNull
    Level level;
    @NonNull
    String value;
}