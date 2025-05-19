package org.darr.mirr.util.function;

import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;

public interface FunctionUtil {

    static <Key, Value, M extends Map<Key, Value>> BinaryOperator<M> concatMap() {
        return (collector, streamItem) -> { collector.putAll(streamItem); return collector; };
    }

    static <T> Predicate<T> predicate(Predicate<T> predicate) {
        return predicate;
    }
}
