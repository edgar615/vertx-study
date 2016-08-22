package com.edgar.util.vertx.task;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * Created by Edgar on 2016/5/6.
 *
 * @author Edgar  Date 2016/5/6
 */
@RunWith(VertxUnitRunner.class)
public class BaseTaskTest {

    Vertx vertx;

    @Before
    public void setUp() {
        vertx = Vertx.vertx();
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.GET, "/")
                .handler(rc -> {
                    vertx.setTimer(2000, l -> rc.response().putHeader("content-type", "application/json")
                            .setStatusCode(200)
                            .end(new JsonObject().put("length", rc.request().getParam("length")).encode()));
                });
        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(9000);
    }

    @After
    public void shutdown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void testMap(TestContext context) {
        Async async = context.async();
        Future<String> future = Future.future();
        future.complete("Hello World");
        Task.create(future)
                .map(s -> s.toUpperCase())
                .andThen(s -> context.assertEquals("HELLO WORLD", s))
                .map(s -> s.length())
                .andThen(length -> {
                    context.assertEquals("Hello World".length(), length);
                    async.complete();
                });
    }

    @Test
    public void testMap2(TestContext context) {
        Async async = context.async();
        Future<String> future = Future.future();
        Task<String> task = Task.create(future);
        task.map(s -> s.toUpperCase())
                .andThen(s -> context.assertEquals("HELLO WORLD", s))
                .map(s -> s.length())
                .andThen(length -> {
                    context.assertEquals("Hello World".length(), length);
                    async.complete();
                });
        task.complete("Hello World");
    }

    @Test
    public void testFailure(TestContext context) {
        Async async = context.async();
        Future<String> future = Future.future();
        future.complete("Hello World");
        Task.create(future)
                .map(s -> Integer.parseInt(s))
                .andThen(i -> context.fail())
                .onFailure(throwable -> {
//                    throwable.printStackTrace();
                    context.assertNotNull(throwable);
//                    async.complete();
                }).andThen(length -> context.fail())
                .onFailure(throwable -> {
                    context.assertNotNull(throwable);
                    async.complete();
                });
    }

    @Test
    public void testFailure2(TestContext context) {
        AtomicInteger seq = new AtomicInteger();
        Async async = context.async();
        Future<String> future = Future.future();
        future.complete("Hello World");
        Task.create(future)
                .map(s -> Integer.parseInt(s))
                .onFailure(throwable -> {
                    seq.incrementAndGet();
                })
                .andThen(i -> context.fail())
                .onFailure(throwable -> {
                    context.assertNotNull(throwable);
                    context.assertEquals(seq.incrementAndGet(), 2);
                    async.complete();
                });
    }

    @Test
    public void testFailure3(TestContext context) {
        Async async = context.async();
        Future<String> future = Future.future();
        future.complete("Hello World");
        Task.create(future)
                .map(s -> "" + s.length())
                .map(s -> Integer.parseInt(s))
                .andThen(i -> System.out.println(i))
                .onFailure(throwable -> {
                    throwable.printStackTrace();
                    context.fail();
//                    async.complete();
                }).andThen(length -> {
            context.assertEquals("Hello World".length(), length);
            async.complete();
        })
                .onFailure(throwable -> {
                    context.fail();
                });
    }

    @Test
    public void testFlatMap(TestContext context) {
        Async async = context.async();
        Future<String> future = Future.future();
        future.complete("Hello World");

        Task.create(future)
                .map(s -> s.length())
                .flatMap(new FutureFunction(vertx))
                .andThen(length -> {
                    System.out.println(length);
                    context.assertEquals("Hello World".length() * 2, length);
                    async.complete();
                }).onFailure(throwable -> throwable.printStackTrace());
    }

    @Test
    public void testFlat2(TestContext context) {
        Async async = context.async();
        Future<String> future = Future.future();
        future.complete("Hello World");
        Task.create(future)
                .map(s -> s.length())
                .flatMap(new Function<Integer, Future<Integer>>() {
                    @Override
                    public Future<Integer> apply(Integer integer) {
                        Future<Integer> rFuture = Future.future();
                        rFuture.complete(integer * 2);
                        return rFuture;
                    }
                })
                .andThen(length -> {
                    context.assertEquals("Hello World".length() * 2, length);
                    async.complete();
                });
    }

    @Test
    public void testFlat3(TestContext context) {
        Async async = context.async();
        Future<String> future = Future.future();
        future.complete("Hello World");
        Task.create(future)
                .map(s -> Integer.parseInt(s))
                .flatMap(new Function<Integer, Future<Integer>>() {
                    @Override
                    public Future<Integer> apply(Integer integer) {
                        Future<Integer> rFuture = Future.future();
                        rFuture.complete(integer * 2);
                        return rFuture;
                    }
                })
                .andThen(length -> {
                    context.fail();
                }).onFailure(throwable -> {
//            throwable.printStackTrace();
            context.assertNotNull(throwable);
            async.complete();
        })
                    ;
    }

    @Test
    public void testFlat4(TestContext context) {
        Async async = context.async();
        Future<String> future = Future.future();
        future.complete("Hello World");
        Task.create(future)
                .map(s -> s.length())
                .flatMap(new Function<Integer, Future<Integer>>() {
                    @Override
                    public Future<Integer> apply(Integer integer) {
                        Future<Integer> rFuture = Future.future();
                        rFuture.complete(integer / 0);
                        return rFuture;
                    }
                })
                .andThen(length -> {
                    context.fail();
                }).onFailure(throwable -> {
//            throwable.printStackTrace();
            context.assertNotNull(throwable);
            async.complete();
        })
        ;
    }

    @Test
    public void testRecover(TestContext context) {
        AtomicInteger seq = new AtomicInteger();
        Async async = context.async();
        Future<String> future = Future.future();
//        future.complete("Hello World");
        Task.create(future)
                .map(s -> Integer.parseInt(s))
                .andThen(i -> context.fail())
                .onFailure(throwable -> seq.incrementAndGet())
                .recover(throwable -> -999)
                .andThen(i -> {
                    context.assertEquals(-999, i);
                    context.assertEquals(seq.get(), 1);
                    async.complete();
                })
                .onFailure(throwable -> {
                    context.fail();
                });
        future.complete("Hello World");
    }

    @Test
    public void testRecover2(TestContext context) {
        Async async = context.async();
        Future<String> future = Future.future();
        future.complete("Hello World");
        Task.create(future)
                .map(s -> Integer.parseInt(s))
                .andThen(i -> context.fail())
                .recover(throwable -> -999)
                .andThen(i -> {
                    context.assertEquals(-999, i);
                    async.complete();
                })
                .onFailure(throwable -> {
                    context.fail();
                }).onTrace(trace -> System.out.println(trace));
    }

    @Test
    public void testRecover3(TestContext context) {
        Async async = context.async();
        Future<String> future = Future.future();
        future.complete("Hello World");
        Task.create(future)
                .map(s -> "" + s.length())
                .map(s -> Integer.parseInt(s))
                .andThen(i -> System.out.println(i))
                .recover(throwable -> -999)
                .andThen(i -> {
                    context.assertEquals("Hello World".length(), i);
                    async.complete();
                })
                .onFailure(throwable -> {
                    context.fail();
                }).onTrace(trace -> System.out.println(trace));
    }

}
