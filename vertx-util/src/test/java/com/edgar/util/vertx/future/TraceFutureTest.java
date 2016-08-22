package com.edgar.util.vertx.future;

import io.vertx.core.Future;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by Edgar on 2016/5/6.
 *
 * @author Edgar  Date 2016/5/6
 */
public class TraceFutureTest {

    @Test
    public void testTrace() throws InterruptedException {
        TraceFuture<Integer> traceFuture = TraceFuture.create();
        TimeUnit.SECONDS.sleep(1);
        traceFuture.complete(1);
        System.out.println("elapsed" + traceFuture.elapsed());

        traceFuture = TraceFuture.create();
        TimeUnit.SECONDS.sleep(1);
        traceFuture.fail("1");
        System.out.println("elapsed" + traceFuture.elapsed());
    }

    @Test
    public void setHandler() throws InterruptedException {
        TraceFuture<Integer> traceFuture = TraceFuture.create();
        traceFuture.setHandler(ar -> {});
        traceFuture.setHandler(ar -> {});
    }

}
