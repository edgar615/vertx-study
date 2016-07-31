package com.edgar.util.vertx.future;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Edgar on 2016/5/6.
 *
 * @author Edgar  Date 2016/5/6
 */
class TraceFutureImpl<T> implements TraceFuture<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TraceFuture.class);
    private final Future<T> delegateFuture;
    private final long startMills = System.currentTimeMillis();
    private boolean handlerSeted = false;
    private long endMills = 0;

    TraceFutureImpl() {
        this.delegateFuture = Future.future();
    }

    TraceFutureImpl(Future<T> delegateFuture) {
        this.delegateFuture = delegateFuture;
    }

    @Override
    public long startMills() {
        return startMills;
    }

    @Override
    public long endMills() {
        return endMills;
    }

    public long elapsed() {
        return endMills - startMills;
    }

    @Override
    public boolean isComplete() {
        return delegateFuture.isComplete();
    }

    @Override
    public Future<T> setHandler(Handler<AsyncResult<T>> handler) {
        if (handlerSeted == true) {
            //打印警告
            LOGGER.warn("task handler has been setted");
        }
        handlerSeted = true;
        return delegateFuture.setHandler(handler);
    }

    @Override
    public void complete(T result) {
        endTrace();
        delegateFuture.complete(result);
    }

    @Override
    public void complete() {
        endTrace();
        delegateFuture.complete();
    }

    @Override
    public void fail(Throwable throwable) {
        endTrace();
        delegateFuture.fail(throwable);
    }

    @Override
    public void fail(String failureMessage) {
        endTrace();
        delegateFuture.fail(failureMessage);
    }

    @Override
    public T result() {
        return delegateFuture.result();
    }

    @Override
    public Throwable cause() {
        return delegateFuture.cause();
    }

    @Override
    public boolean succeeded() {
        return delegateFuture.succeeded();
    }

    @Override
    public boolean failed() {
        return delegateFuture.failed();
    }

    @Override
    public void handle(AsyncResult<T> ar) {
        if (ar.succeeded()) {
            complete(ar.result());
        } else {
            fail(ar.cause());
        }
    }

    private void endTrace() {
        endMills = System.currentTimeMillis();
    }
}
