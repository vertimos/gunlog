package com.bf.log.gunlog;

import java.io.InputStream;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.bf.log.api.Log;
import com.bf.log.api.LogParser;

public class GunLogParser implements LogParser {

    @Override
    public Stream<Log> parse(InputStream source, Pattern pattern) {
        return null;
    }

}