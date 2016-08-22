package com.edgar.util.vertx.task;

/**
 * Created by Edgar on 2016/7/26.
 *
 * @author Edgar  Date 2016/7/26
 */
public interface Consumer3<T1, T2, T3> {
    void accept(T1 t1, T2 t2, T3 t3);
}
