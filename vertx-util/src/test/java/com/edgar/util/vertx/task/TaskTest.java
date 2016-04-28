package com.edgar.util.vertx.task;

import io.vertx.core.Future;
import io.vertx.core.Vertx;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by edgar on 16-4-28.
 */
public class TaskTest {
  public static void main(String[] args) {
//    Future<String> future = Future.future();
//    Task<Integer> task = new Task<>(Vertx.vertx(), future)
//            .map(new Function<String, Integer>() {
//              @Override
//              public Integer apply(String s) {
//                return s.length();
//              }
//            }).andThen(new Consumer<Integer>() {
//              @Override
//              public void accept(Integer integer) {
//                System.out.println("the length:" + integer);
//              }
//            }).map(new Function<Integer, Integer>() {
//              @Override
//              public Integer apply(Integer integer) {
//                return integer * 10;
//              }
//            }).andThen(new Consumer<Integer>() {
//              @Override
//              public void accept(Integer integer) {
//                System.out.println("then * 10 = " + integer);
//              }
//            });
//    future.complete("Hello world");
//    System.out.println(task.getFuture().result());

//    Future<Integer> future = Future.future();
//    Task<Integer> task = new Task<>(Vertx.vertx(), future)
//            .andThen(new Consumer<Integer>() {
//              @Override
//              public void accept(Integer integer) {
//                System.out.println(integer);
//              }
//            }).onFailure(new Consumer<Throwable>() {
//              @Override
//              public void accept(Throwable throwable) {
//                throwable.printStackTrace();
//              }
//            });
//    future.fail("oh no");
//    System.out.println(task.getFuture().result());

        Future<String> future = Future.future();
    Task<Integer> task = new Task<>(Vertx.vertx(), future)
            .map(new Function<String, Integer>() {
              @Override
              public Integer apply(String s) {
                return s.length();
              }
            }).andThen(new Consumer<Integer>() {
              @Override
              public void accept(Integer integer) {
                System.out.println("the length:" + integer);
              }
            }).map(new Function<Integer, Integer>() {
              @Override
              public Integer apply(Integer integer) {
                return integer * 10;
              }
            }).andThen(new Consumer<Integer>() {
              @Override
              public void accept(Integer integer) {
                System.out.println("then * 10 = " + integer);
              }
            });
    Future<String> future2 = Future.future();
    Task<String> task2 = new Task<>(Vertx.vertx(), future2)
            .map(new Function<String, String>() {
              @Override
              public String apply(String s) {
                return s.toUpperCase();
              }
            })
            .andThen(new Consumer<String>() {
              @Override
              public void accept(String s) {
                System.out.println(s);
              }
            });
    Task<String> task3 = task.flatMap(task2);
    future.complete("Hello world");
    future2.complete("Hello world");
    System.out.println(task3.getFuture().isComplete());
  }
}
