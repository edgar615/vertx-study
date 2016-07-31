package com.edgar.util.vertx.task;

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

    default <R> Task<R> map(String desc, BiFunction<T1, T2, R> function) {
        return map(desc, t -> function.apply(t.getT1(), t.getT2()));
    }

    default Tuple2Task<T1, T2> andThen(String desc, BiConsumer<T1, T2> consumer) {
        return cast(andThen(desc, t -> consumer.accept(t.getT1(), t.getT2())));
    }

//    <R> Task<R> flatMap(String desc, Consumer3<T1, T2, Task<R>> consumer);
//
//    <R> Task<R> flatMap(String desc, BiFunction<T1, T2, Future<R>> function);

    /**
     * 处理任务遇到的异常.
     * 异常会一直在task中传播，所以通常只需要在task链的最后一个task上捕获即可。
     *
     * @param failureHandler 异常处理对象
     * @return task
     */
//    Tuple2Task<T1, T2> onFailure(Consumer<Throwable> failureHandler);
}
