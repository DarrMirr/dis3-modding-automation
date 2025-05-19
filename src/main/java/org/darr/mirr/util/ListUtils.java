package org.darr.mirr.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ListUtils {

    public static <T> List<T> makeListNonImmutable(List<T> list) {
        return list instanceof ArrayList<T> ? list : new ArrayList<>(list);
    }
}
