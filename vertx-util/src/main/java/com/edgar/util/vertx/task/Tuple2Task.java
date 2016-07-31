package com.edgar.util.vertx.task;

import io.vertx.core.Future;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * Created by Edgar on 2016/7/26.
 *
 * @author Edgar  Date 2016/7/26
 */
public interface Tuple2Task<T1, T2> extends Task<Tuple2<T1, T2>> {

    public static <T1, T2> Tuple2Task<T1, T2> cast(final Task<Tuple2<T1, T2>> task) {
        return new Tuple2TaskDelegate<>(task);
    }

    default <R> Task<R> map(BiFunction<T1, T2, R> function) {
        return map("map: " + function.getClass().getName(), function);
    }

    default <R> Task<R> map(String desc, BiFunction<T1, T2, R> function) {
        return map(desc, t -> function.apply(t.getT1(), t.getT2()));
    }

    default Tuple2Task<T1, T2> andThen(BiConsumer<T1, T2> consumer) {
        return andThen("andThen: " + consumer.getClass().getName(), consumer);
    }

    default Tuple2Task<T1, T2> andThen(String desc, BiConsumer<T1, T2> consumer) {
        return cast(andThen(desc, t -> consumer.accept(t.getT1(), t.getT2())));
    }

    default <R> Task<R> flatMap(BiFunction<T1, T2, Future<R>> function) {
        return flatMap("map: " + function.getClass().getName(), t -> function.apply(t.getT1(), t.getT2()));
    }

    default <R> Task<R> flatMap(String desc, BiFunction<T1, T2, Future<R>> function) {
        return flatMap(desc, t -> function.apply(t.getT1(), t.getT2()));
    }
}
