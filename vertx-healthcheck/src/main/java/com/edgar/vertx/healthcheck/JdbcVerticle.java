package com.edgar.vertx.healthcheck;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.Status;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.web.Router;

/**
 * Created by edgar on 17-3-9.
 */
public class JdbcVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(JdbcVerticle.class.getName());
  }

  @Override
  public void start() throws Exception {
    final JDBCClient jdbcClient = JDBCClient.createShared(vertx, new JsonObject()
            .put("driver_class", "com.mysql.jdbc.Driver")
            .put("url", "jdbc:mysql://localhost:3306/user")
            .put("max_pool_size", 30)
            .put("user", "admin")
    );

    HealthCheckHandler healthCheckHandler = HealthCheckHandler.create(vertx);

    Router router = Router.router(vertx);
    router.get("/health").handler(healthCheckHandler);

    healthCheckHandler.register("datasource", future -> {
      jdbcClient.getConnection(ar -> {
        if (ar.succeeded()) {
          ar.result().close();
          future.complete(Status.OK());
        } else {
          future.fail(ar.cause());
        }
      });
    });

    vertx.createHttpServer()
        .requestHandler(router::accept)
        .listen(8080);

  }
}
