package com.bf.log.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MutableContextLog implements ContextLog {
    private final Map<String, Object> ctx = new HashMap<>();
    private final Log log;

    private MutableContextLog(Log log) {
        this.log = log;
    }

    public static MutableContextLog withContext(Log log) {
        return new MutableContextLog(log);
    }

    public Log value() {
        return log;
    }

    @Override
    public Optional<Integer> getInt(String key) {
        try {
            return Optional.ofNullable((Integer) ctx.get(key));
        } catch (ClassCastException ex) {
            throw new ContextReadException("Property '" + key + "' could not be cast to Integer.",
                    ex);
        }
    }

}
