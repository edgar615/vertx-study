package com.edgar.util.vertx.task;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * vert.x异步任务的工具类.
 * <p>
 * taks大部分的设计都参考了linkedin的parseq实现.
 *
 * @author Edgar  Date 2016/5/10
 */
public interface Task<T> {

    public static <T> Task<T> create(String name, Future<T> future) {
        return new BaseTask<>(name, future);
    }

    public static <T> Task<T> create(Future<T> future) {
        return new BaseTask<>(future);
    }

    public static <T1, T2> Tuple2Task<T1, T2> par(Future<T1> futureT1, Future<T2> futureT2) {
        return new Tuple2TaskImpl<>(futureT1, futureT2);
    }

    T result();

    /**
     * 任务名称
     *
     * @return 名称
     */
    String name();

    /**
     * 任务成功完成.
     *
     * @param result 任务的成功返回值
     */
    void complete(T result);

    /**
     * 任务异常完成.
     *
     * @param throwable 任务的异常
     */
    void fail(Throwable throwable);

    /**
     * 处理任务遇到的异常.
     * 异常会一直在task中传播，所以通常只需要在task链的最后一个task上捕获即可。
     *
     * @param failureHandler 异常处理对象
     * @return task
     */
    default Task<T> onFailure(Consumer<Throwable> failureHandler) {
        return new FusionTask<>(null, this, (prev, next) -> {
            prev.setHandler(ar -> {
                Throwable throwable = ar.cause();
                if (ar.succeeded()) {
                    next.complete(prev.result());
                }
                if (throwable != null) {
                    try {
                        failureHandler.accept(throwable);
                    } catch (Exception e) {
                        throwable = e;
                    }
                    next.fail(throwable);
                }
            });
        });
    }

    boolean isComplete();

    /**
     * 处理任务的日志.
     * 日志会一直在task中传播，所以通常只需要在task链的最后一个task上处理即可.
     *
     * @param traceHandler 日志的处理对象
     * @return task
     */
    Task<T> onTrace(Consumer<List<Trace>> traceHandler);


    void setHandler(Handler<AsyncResult<T>> handler);

    /**
     * 将task从异常中恢复
     *
     * @param desc     任务描述
     * @param function 异常转换类
     * @return 新任务
     */
    // Task<T> recover(String desc, Function<Throwable, T> function);
    default Task<T> recover(String desc, Function<Throwable, T> function) {
        return new FusionTask<>(null, this, (prev, next) -> {
            prev.setHandler(ar -> {
                Throwable throwable = ar.cause();
                if (ar.succeeded()) {
                    next.complete(prev.result());
                }
                if (throwable != null) {
                    try {
                        next.complete(function.apply(throwable));
                    } catch (Exception e) {
                        throwable = e;
                        next.fail(throwable);
                    }
                }
            });
        });
    }

    /**
     * 任务完成之后，将结果转换为其他对象.
     *
     * @param function function类
     * @param <R>      转换后的类型
     * @return task
     */
    default <R> Task<R> map(Function<T, R> function) {
        return map("map: " + function.getClass().getName(), function);
    }

    /**
     * 任务完成之后，将结果转换为其他对象.
     *
     * @param desc     任务描述
     * @param function function类
     * @param <R>      转换后的类型
     * @return task
     */
    default <R> Task<R> map(String desc, Function<T, R> function) {
        return new FusionTask<>(desc, this, (prev, next) -> {
            prev.setHandler(ar -> {
                Throwable throwable = ar.cause();
                if (ar.succeeded()) {
                    try {
                        next.complete(function.apply(ar.result()));
                    } catch (Exception e) {
                        throwable = e;
                    }
                }
                if (throwable != null) {
                    next.fail(throwable);
                }
            });
        });
    }

    /**
     * 任务完成后，根据结果做一些额外操作.
     *
     * @param desc     任务描述
     * @param consumer consumer类
     * @return task
     */
    default Task<T> andThen(String desc, Consumer<T> consumer) {
        return map(desc, t -> {
            consumer.accept(t);
            return t;
        });
    }

    /**
     * 任务完成后，根据结果做一些额外操作.
     *
     * @param consumer consumer类
     * @return task
     */
    default Task<T> andThen(Consumer<T> consumer) {
        return andThen("andThen: " + consumer.getClass().getName(), consumer);
    }

    /**
     * 任务完成之后，让结果传递给另外一个任务执行。
     * 在consumer方法中通过task.complete或者task.fail方法来设置新任务的执行结果,
     * 从而达到传播任务的目的.
     *
     * @param function function类，将结果转换为一个新的future
     * @param <R>      转换后的类型
     * @return task
     */
    default <R> Task<R> flatMap(Function<T, Future<R>> function) {
        return flatMap("map: " + function.getClass().getName(), function);
    }

    /**
     * 任务完成之后，让结果传递给另外一个任务执行，futureFunction用来使用结果创建一个新的任务
     *
     * @param desc     任务描述
     * @param function function类，将结果转换为一个新的future
     * @param <R>
     * @return
     */
    default <R> Task<R> flatMap(String desc, Function<T, Future<R>> function) {
        return new FusionTask<>(desc, this, (prev, next) -> {
            prev.setHandler(ar -> {
                Throwable throwable = ar.cause();
                if (ar.succeeded()) {
                    try {
                        Future<R> rFuture = function.apply(ar.result());
                        rFuture.setHandler(next.completer());
                    } catch (Exception e) {
                        throwable = e;
                    }
                }
                if (throwable != null) {
                    next.fail(throwable);
                }
            });
        });
    }

    /**
     * 将task从异常中恢复
     *
     * @param function 异常转换类
     * @return 新任务
     */
    default Task<T> recover(Function<Throwable, T> function) {
        return recover("recover: " + function.getClass().getName(), function);
    }

    Handler<AsyncResult<T>> completer();
}
