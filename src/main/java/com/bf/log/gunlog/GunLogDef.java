package com.bf.log.gunlog;

import java.util.function.BiFunction;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import com.bf.log.api.Log;

import lombok.Value;

@Value(staticConstructor = "of")
public class GunLogDef {
    Pattern pattern;
    BiFunction<MatchResult, String, Log> logFactory;
}