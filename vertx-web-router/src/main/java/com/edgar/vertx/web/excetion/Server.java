package com.edgar.vertx.web.excetion;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * Created by Edgar on 2016/4/14.
 *
 * @author Edgar  Date 2016/4/14
 */
public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    router.route(HttpMethod.GET, "/users")
            .handler(rc -> {
              throw new RuntimeException();
            });

    router.route(HttpMethod.GET, "/users/400")
            .handler(rc -> {
              rc.response().putHeader("content-type", "application/json")
                      .setStatusCode(400)
                      .end(new JsonArray().add(new JsonObject().put("userId", 1))
                                   .add(new JsonObject().put("userId", 2))
                                   .add(new JsonObject().put("userId", 3))
                                   .add(new JsonObject().put("userId", 4))
                                   .encode());
            });
    router.route().failureHandler(rc -> {
      rc.failure().printStackTrace();
      rc.response().putHeader("content-type", "application/json")
              .setStatusCode(400)
              .end(new JsonObject().put("msg", rc.failure().getMessage()).encode());
    });
    vertx.createHttpServer()
            .requestHandler(router::accept)
            .listen(8080, result -> {
              if (result.succeeded()) {
                startFuture.complete();
              } else {
                startFuture.fail(result.cause());
              }
            });
  }
}
