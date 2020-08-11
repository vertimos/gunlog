package com.bf.log.api;

import java.util.Optional;

public interface ContextLog extends Log {
    Optional<String> getString(String key);

    Optional<Integer> getInt(String key);

    Optional<Double> getDouble(String key);

    class ContextReadException extends RuntimeException {
        public ContextReadException(String msg, Throwable thr) {
            super(msg, thr);
        }
    }
}
