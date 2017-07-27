package com.edgar.vertx.helloword;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CompletableFuture;

@RunWith(VertxUnitRunner.class)
public class ImmediateCompletionTest {
    @Rule
    public final RunTestOnContext rule = new RunTestOnContext();

    //线程未变，toComplete.thenRun运行在正确的线程上，但是不在正确的context上,所以不会调用context上的异常处理函数，这个方法会一直阻塞
    @Test
    public void testBlock(TestContext context) {
        final Async async = context.async();
        final Vertx vertx = rule.vertx();
        final CompletableFuture<Integer> toComplete = new CompletableFuture<>();
        // delay future completion by 500 ms
        final String threadName = Thread.currentThread().getName();
        System.out.println(threadName);
        System.out.println(Vertx.currentContext());
        toComplete.complete(100);
        vertx.getOrCreateContext().exceptionHandler(throwable -> {
            throwable.printStackTrace();
            async.complete();
        });
        toComplete.thenRun(() -> {
            System.out.println(Thread.currentThread().getName());
            System.out.println(Vertx.currentContext());
            context.assertEquals(Thread.currentThread().getName(), threadName);
            throw new RuntimeException("a");
//            async.complete();
        });
    }

    @Test
    public void testImmediateCompletion(TestContext context) {
        final Async async = context.async();
        final Vertx vertx = rule.vertx();
        final CompletableFuture<Integer> toComplete = new CompletableFuture<>();
        // delay future completion by 500 ms
        final String threadName = Thread.currentThread().getName();
        System.out.println(threadName);
        System.out.println(Vertx.currentContext());
        toComplete.complete(100);
        vertx.getOrCreateContext().exceptionHandler(throwable -> {
            throwable.printStackTrace();
            async.complete();
        });
        final Context currentContext = vertx.getOrCreateContext();
        toComplete.thenRun(() -> {
            System.out.println(Vertx.currentContext());
            currentContext.runOnContext(v -> {
                System.out.println(Thread.currentThread().getName());
                System.out.println(Vertx.currentContext());
                context.assertEquals(Thread.currentThread().getName(), threadName);
                throw new RuntimeException("a");
//                async.complete();
            });
        });
    }
}