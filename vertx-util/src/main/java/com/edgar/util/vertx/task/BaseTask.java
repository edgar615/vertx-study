package com.edgar.util.vertx.task;

import com.edgar.util.vertx.future.TraceFuture;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Edgar on 2016/5/9.
 *
 * @author Edgar  Date 2016/5/9
 */
public class BaseTask<T> implements Task<T>, AsyncResult<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseTask.class);

    /**
     * future
     */
    private final Future<T> future;

    /**
     * 日志跟踪
     */
    private final List<Trace> traceList = new ArrayList<>();

    private final Trace trace;

    private String name;

    /**
     * 日志处理
     */
    private Consumer<List<Trace>> traceHandler;

    BaseTask(String name, Future<T> future) {
        this.name = name;
        if (future instanceof TraceFuture) {
            this.future = future;
        } else {
            this.future = TraceFuture.create(future);
        }
        this.trace = Trace.create(name);
        this.traceList.add(trace);
    }

    BaseTask(Future<T> future) {
        this(null, future);
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public void complete(T result) {
        future.complete(result);
        doTrace();
    }

    @Override
    public void fail(Throwable throwable) {
        future.fail(throwable);
        doTrace();
    }

    @Override
    public T result() {
        return future.result();
    }

    @Override
    public Throwable cause() {
        return future.cause();
    }

    @Override
    public boolean succeeded() {
        return future.succeeded();
    }

    @Override
    public boolean failed() {
        return future.failed();
    }

    @Override
    public boolean isComplete() {
        return future.isComplete();
    }

    @Override
    public Task<T> onTrace(Consumer<List<Trace>> traceHandler) {
        this.traceHandler = traceHandler;
        LOGGER.trace("set traceHandler on task:{}", name);
        if (isComplete()) {
            traceHandler.accept(traceList);
        }
        return this;
    }

    private void doTrace() {
        if (traceHandler != null) {
            LOGGER.trace("exec traceHandler on task:{}", name);
            List<Trace> copyTraceList = new ArrayList<Trace>(traceList);
            Collections.reverse(copyTraceList);
            traceHandler.accept(copyTraceList);
        }
    }

    @Override
    public void setHandler(Handler<AsyncResult<T>> handler) {
        future.setHandler(handler);
    }

    /**
     * 将当前任务的日志填充到另外一个任务中
     *
     * @param task    需要填充日志的任务
     * @param handler 处理类
     * @param <R>     泛型
     */
    private <R> void wrapTrace(BaseTask<R> task, Handler<AsyncResult<T>> handler) {//日志跟踪
//    Trace trace = Trace.create(this.name);
        future.setHandler(ar -> {
            if (ar.succeeded()) {
                trace.success();
            } else {
                trace.fail();
            }
            handler.handle(ar);
        });
        this.traceList.forEach(t -> task.addTrace(t));
//    task.addTrace(trace);
    }

    private void addTrace(Trace trace) {
        traceList.add(trace);
    }

    @Override
    public Handler<AsyncResult<T>> completer() {
        return future.completer();
    }
}
