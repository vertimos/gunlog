package com.bf.log.api;

import java.io.InputStream;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public interface LogParser {
    Stream<Log> parse(InputStream source, Pattern pattern);
}