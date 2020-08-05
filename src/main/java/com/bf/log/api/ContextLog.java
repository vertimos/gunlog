package com.bf.log.api;

import java.util.Optional;

public interface ContextLog {
    Optional<Integer> getInt(String key);

    public static class ContextReadException extends RuntimeException {
        private static final long serialVersionUID = 5314781485780522841L;

        ContextReadException(String msg, Throwable thr) {
            super(msg, thr);
        }
    }
}
