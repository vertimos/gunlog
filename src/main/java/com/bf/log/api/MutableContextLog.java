package com.bf.log.api;

public interface MutableContextLog extends ContextLog {
    MutableContextLog put(String key, String value);
}
