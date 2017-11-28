package com.edgar.vertx.jwt;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
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

    KeyStoreOptions keyStoreOptions = new KeyStoreOptions(new JsonObject()
                                                                  .put("path", "keystore.jceks")
                                                                  .put("type", "jceks")
                                                                  .put("password", "secret"));

    JWTAuth provider = JWTAuth.create(vertx, new JWTAuthOptions().setKeyStore(keyStoreOptions));

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
          String expToken =
                  provider.generateToken(new JsonObject().put("sub", "paulo"), new JWTOptions().setExpiresInSeconds(10000l));
        // now for any request to protected resources you should pass this string in the HTTP
        // header Authorization as:
        // Authorization: Bearer <token>

//          iss: token的发行者
//          sub: token的题目
//          aud: token的客户
//          exp: 经常使用的，以数字时间定义失效期，也就是当前时间以后的某个时间本token失效。
//          nbf: 定义在此时间之前，JWT不会接受处理。
//          iat: JWT发布时间，能用于决定JWT年龄
//          jti: JWT唯一标识. 能用于防止 JWT重复使用，一次只用一个token

          rc.response().putHeader("content-type", "application/json").end(
                new JsonObject().put("token", token)
                        .put("expToken", expToken).encode()
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
