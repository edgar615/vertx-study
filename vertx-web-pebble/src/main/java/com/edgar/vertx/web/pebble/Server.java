package com.edgar.vertx.web.pebble;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.templ.PebbleTemplateEngine;
import io.vertx.ext.web.templ.TemplateEngine;

public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(Server.class.getName());
  }

  @Override
  public void start() throws Exception {
//      http://www.mitchellbosecke.com/pebble/documentation
    //只有handlebars支持解析json
    TemplateEngine engine = PebbleTemplateEngine.create(vertx);
//
    Router router = Router.router(vertx);

    router.get("/home").handler(rc -> {
      rc.put("layout", "base");
      engine.render(rc, "template/home.peb", res -> {

        if (res.succeeded()) {
          rc.response().end(res.result());
        } else {
          rc.fail(res.cause());
        }
      });
    });

    router.get().handler(rc -> {
      rc.put("name", "Vert.x Web");
      rc.put("user", new JsonObject().put("score", 30).getMap());
      rc.put("highscore", 50);

      rc.put("articles", new JsonArray().add(new JsonObject().put("title", "a").put("content", "hoho"))
          .add(new JsonObject().put("title", "b").put("content", "haha")));

      rc.put("category", "hoho");
      engine.render(rc, "template/index.peb", res -> {

        if (res.succeeded()) {
          rc.response().end(res.result());
        } else {
          rc.fail(res.cause());
        }
      });
    });
    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
  }
}