package com.bf.log.gunlog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringUtils {

    private StringUtils() {
    }

    public static String lines(String... lines) {
        return Stream.of(lines).collect(Collectors.joining(System.lineSeparator()));
    }

    public static InputStream asInputStream(String log) {
        return new ByteArrayInputStream(log.getBytes(StandardCharsets.UTF_8));
    }
}
