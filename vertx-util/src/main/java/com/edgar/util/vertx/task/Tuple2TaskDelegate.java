package com.edgar.util.vertx.task;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by Edgar on 2016/7/29.
 *
 * @author Edgar  Date 2016/7/29
 */
public class Tuple2TaskDelegate<T1, T2> implements Tuple2Task<T1, T2> {
    private final Task<Tuple2<T1, T2>> task;

    public Tuple2TaskDelegate(Task<Tuple2<T1, T2>> task) {
        this.task = task;
    }

    @Override
    public Tuple2<T1, T2> result() {
        return null;
    }

    @Override
    public String name() {
        return task.name();
    }

    @Override
    public void complete(Tuple2<T1, T2> result) {
        task.complete(result);
    }

    @Override
    public void fail(Throwable throwable) {
        task.fail(throwable);
    }

    @Override
    public boolean isComplete() {
        return task.isComplete();
    }

    @Override
    public Task<Tuple2<T1, T2>> onTrace(Consumer<List<Trace>> traceHandler) {
        return task.onTrace(traceHandler);
    }

    @Override
    public void setHandler(Handler<AsyncResult<Tuple2<T1, T2>>> handler) {
        task.setHandler(handler);
    }

    @Override
    public Task<Tuple2<T1, T2>> recover(String desc, Function<Throwable, Tuple2<T1, T2>> function) {
        return task.recover(desc, function);
    }

    @Override
    public Handler<AsyncResult<Tuple2<T1, T2>>> completer() {
        return task.completer();
    }
}
