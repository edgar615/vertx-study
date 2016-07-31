package com.edgar.util.vertx.task;

/**
 * Created by edgar on 16-6-2.
 */
public class OtherFuture<T> {

    private String key;

    private T result;

    /**
     * 根据前一个值执行下一步操作
     * @param prevResult
     */
    public void start(T prevResult) {

    }

    public void complete() {

    }
}
