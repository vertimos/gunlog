package com.bf.log.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class Context {
    private final Map<String, Object> ctx = new HashMap<>();

    public Optional<Integer> getInt(String key) {
        try {
            return Optional.ofNullable((Integer) ctx.get(key));
        } catch (ClassCastException ex) {
            throw new ContextReadException("Property '" + key + "' could not be cast to Integer.", ex);
        }
    }

    public static class ContextReadException extends RuntimeException {
        private static final long serialVersionUID = 5314781485780522841L;

        ContextReadException(String msg, Throwable thr) {
            super(msg, thr);
        }
    }
}
