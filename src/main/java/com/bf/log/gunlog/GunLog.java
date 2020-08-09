package com.bf.log.gunlog;

import com.bf.log.api.MutableContextLog;
import lombok.NonNull;
import lombok.Value;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

@Value(staticConstructor = "of")
public class GunLog implements MutableContextLog {
    Map<String, Object> ctx = new HashMap<>();

    @NonNull
    Instant time;
    @NonNull
    Level level;
    @NonNull
    String value;

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