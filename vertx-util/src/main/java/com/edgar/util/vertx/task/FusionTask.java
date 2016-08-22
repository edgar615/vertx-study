package com.edgar.util.vertx.task;

import io.vertx.core.Future;

/**
 * Created by Edgar on 2016/7/29.
 *
 * @author Edgar  Date 2016/7/29
 */
class FusionTask<T, R> extends BaseTask<R> {

    FusionTask(String name, Task<T> prevTask, TaskTransformer<T, R> taskTransformer) {
        super(name, Future.<R>future());
        taskTransformer.accept(prevTask, this);
    }
}
