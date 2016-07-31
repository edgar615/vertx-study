package com.edgar.util.vertx.task;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.function.Function;

/**
 * Created by Edgar on 2016/7/28.
 *
 * @author Edgar  Date 2016/7/28
 */
public class FutureFunction implements Function<Integer, Future<Integer>> {

    private final Vertx vertx;

    public FutureFunction(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public Future<Integer> apply(Integer length) {
        Future<Integer> future = Future.future();
        vertx.createHttpClient().get(9000, "localhost", "/?length=" + length, response -> {
            response.bodyHandler(body -> {
                JsonObject jsonObject = body.toJsonObject();
                future.complete(Integer.parseInt(jsonObject.getString("length")) * 2);
            });
        }).end();

        return future;
    }
}
