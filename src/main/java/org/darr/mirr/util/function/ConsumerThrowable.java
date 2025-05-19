package org.darr.mirr.util.function;

/**
 * Sibling interface of {@link java.util.function.Consumer}
 * with additional declaration of throwing Exception at function method.
 *
 * @param <T> parameterized type.
 */
@FunctionalInterface
public interface ConsumerThrowable<T> {

    void accept(T t) throws Exception;
}
