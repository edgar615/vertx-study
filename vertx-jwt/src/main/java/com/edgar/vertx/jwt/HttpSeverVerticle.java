package com.edgar.vertx.jwt;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * Created by Edgar on 2016/4/13.
 *
 * @author Edgar  Date 2016/4/13
 */
public class HttpSeverVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(HttpSeverVerticle.class);
  }


  @Override
  public void start() throws Exception {
    JsonObject config = new JsonObject().put("keyStore", new JsonObject()
            .put("path", "keystore.jceks")
            .put("type", "jceks")
            .put("password", "secret"));

    JWTAuth provider = JWTAuth.create(vertx, config);

    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
//    router.post("/login").handler(rc -> {
//      System.out.println(rc.getBody());
//      rc.response().putHeader("content-type", "application/json").end(new JsonObject().put("foo", "bar").encode());
//    });
    router.post("/login").handler(rc -> {
      JsonObject data = rc.getBodyAsJson();
      String username = data.getString("username");
      String password = data.getString("password");
      if ("paulo".equals(username) && "super_secret".equals(password)) {
        String token =
                provider.generateToken(new JsonObject().put("sub", "paulo"), new JWTOptions());
        // now for any request to protected resources you should pass this string in the HTTP
        // header Authorization as:
        // Authorization: Bearer <token>
        rc.response().putHeader("content-type", "application/json").end(
                new JsonObject().put("token", token).encode()
        );
      } else {
        rc.response().putHeader("content-type", "application/json")
                .setStatusCode(403).end("auth");
      }
    });
    vertx.createHttpServer().requestHandler(router::accept)
            .listen(8080);
  }
}
