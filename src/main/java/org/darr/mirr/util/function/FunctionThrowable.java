package org.darr.mirr.util.function;

/**
 * Sibling interface of {@link java.util.function.Function}
 * with additional declaration of throwing Exception at function method.
 *
 * @param <T> parameterized type.
 * @param <R> parameterized type.
 */
@FunctionalInterface
public interface FunctionThrowable<T, R> {

    R apply(T t) throws Exception;
}
