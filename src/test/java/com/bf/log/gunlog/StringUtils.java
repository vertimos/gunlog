package com.bf.log.gunlog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {
    String lines(String... lines) {
        return Stream.of(lines).collect(Collectors.joining(System.lineSeparator()));
    }

    InputStream asInputStream(String log) {
        return new ByteArrayInputStream(log.getBytes(StandardCharsets.UTF_8));
    }
}
