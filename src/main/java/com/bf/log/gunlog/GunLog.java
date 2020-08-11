package com.bf.log.gunlog;

import com.bf.log.api.MutableContextLog;
import lombok.NonNull;
import lombok.Value;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Value(staticConstructor = "of")
public class GunLog implements MutableContextLog {
    Map<String, String> ctx = new HashMap<>();

    @NonNull
    Instant time;
    @NonNull
    String level;
    @NonNull
    String value;

    @Override
    public Optional<String> getString(String key) {
        return Optional.ofNullable(ctx.get(key));
    }

    @Override
    public Optional<Integer> getInt(String key) {
        return get(key, Integer::parseInt, Integer.class);
    }

    @Override
    public Optional<Double> getDouble(String key) {
        return get(key, Double::parseDouble, Double.class);
    }

    private <T> Optional<T> get(String key, Function<String, T> mapper, Class<T> clazz) {
        try {
            return Optional.ofNullable(ctx.get(key)).map(mapper);
        } catch (ClassCastException ex) {
            throw new ContextReadException("Property '" + key + "' could not be casted to " + clazz,
                    ex);
        }
    }

    @Override
    public GunLog put(String key, String value) {
        ctx.put(key, value);
        return this;
    }
}