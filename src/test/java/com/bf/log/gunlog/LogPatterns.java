package com.bf.log.gunlog;

import java.util.regex.Pattern;

public class LogPatterns {
    private LogPatterns() {
    }

    public static Pattern TEST_LOG_PATTERN = Pattern.compile(
            "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}(?:\\.\\d{1,3})?) \\[.*\\] (DEBUG|INFO|WARN|ERROR) .*");
}
