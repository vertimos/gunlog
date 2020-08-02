package com.bf.log.api;

import java.time.Instant;
import java.util.logging.Level;

import lombok.Value;

@Value
public class Log {
    Instant time;
    Level level;
    String value;
}