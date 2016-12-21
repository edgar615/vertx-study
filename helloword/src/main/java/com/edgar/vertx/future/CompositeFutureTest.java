package com.edgar.vertx.future;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;

/**
 * Created by Edgar on 2016/3/14.
 *
 * @author Edgar  Date 2016/3/14
 */
public class CompositeFutureTest {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    Future<String> future1 = Future.future();
    Future<String> future2 = Future.future();
    CompositeFuture future = CompositeFuture.all(future1, future2).setHandler(ar -> {
      if (ar.succeeded()) {
        CompositeFuture f = ar.result();
        System.out.println(f.result(0).toString());
        System.out.println(f.result(1).toString());
      } else {
        System.err.println("error" + ar.cause().getMessage());
      }
    });
    vertx.createHttpClient().get(80, "www.baidu.com", "/", response -> {
      response.bodyHandler(body -> {
        future1.complete(body.toString());
      });
    }).end();

    vertx.createHttpClient().get(80, "www.baidu.com", "/", response -> {
      response.bodyHandler(body -> {
        future2.complete(body.toString());
      });
    }).end();



//    compose can be used for chaining futures:

//    FileSystem fs = vertx.fileSystem();
//
//    Future<Void> fut1 = Future.future();
//    Future<Void> fut2 = Future.future();
//
//    fs.createFile("/foo", fut1.completer());
//    fut1.compose(v -> {
//      fs.writeFile("/foo", Buffer.buffer(), fut2.completer());
//    }, fut2);
//    fut2.compose(v -> {
//      fs.move("/foo", "/bar", startFuture.completer());
//    }, startFuture);


  }
}
