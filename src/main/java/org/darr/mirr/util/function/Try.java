package org.darr.mirr.util.function;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Interface provide capability to use throwable function in map() java methods
 * without using try-catch block in function body.
 */
public interface Try {

    static <T, R> Function<T, R> of(FunctionThrowable<T, R> functionThrowable) {
        return t -> {
            try {
                return functionThrowable.apply(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    static <T> Consumer<T> of(ConsumerThrowable<T> consumerThrowable) {
        return t -> {
            try {
                consumerThrowable.accept(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
