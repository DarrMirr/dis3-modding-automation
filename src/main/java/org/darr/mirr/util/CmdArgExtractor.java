package org.darr.mirr.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CmdArgExtractor {

    public static String getArgMandatory(String[] args, String name) {
        return Stream
                .of(args)
                // TODO: 04.12.2024: it is quite unsafe. There is needs for ArgParser
                .filter(arg -> arg.startsWith(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Mandatory parameter '" + name + "' is not present"));
    }

    public static String getArgValue(String param) {
        var index = param.indexOf("=");
        return param.substring(index + 1);
    }
}
