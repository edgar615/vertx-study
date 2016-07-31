package com.edgar.util.vertx.task;

import java.util.function.BiConsumer;

/**
 * Created by Edgar on 2016/7/28.
 *
 * @author Edgar  Date 2016/7/28
 */
public interface TaskTransformer<S, T> extends BiConsumer<Task<S>, Task<T>> {

}
