package com.bf.log.api;

import java.util.Optional;

public interface ContextLog extends Log {
    Optional<Integer> getInt(String key);

    class ContextReadException extends RuntimeException {
        public ContextReadException(String msg, Throwable thr) {
            super(msg, thr);
        }
    }
}
