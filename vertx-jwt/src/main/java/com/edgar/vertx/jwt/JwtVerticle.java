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

import java.util.Base64;

/**
 * Created by Edgar on 2016/4/13.
 *
 * @author Edgar  Date 2016/4/13
 */
public class JwtVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(JwtVerticle.class);
  }


  @Override
  public void start() throws Exception {

    KeyStoreOptions keyStoreOptions = new KeyStoreOptions(new JsonObject()
                                                                  .put("path", "keystore.jceks")
                                                                  .put("type", "jceks")
                                                                  .put("password", "secret"));

    JWTAuth provider = JWTAuth.create(vertx, new JWTAuthOptions().setKeyStore(keyStoreOptions));

    String token =
            provider.generateToken(new JsonObject().put("sub", "paulo"), new JWTOptions()
                    .addHeader("foo", "bar")
                    .setNoTimestamp(false));
    System.out.println(token);
    String[] tokens = token.split("\\.");
    System.out.println(new String(Base64.getDecoder().decode(tokens[0])));
    System.out.println(new String(Base64.getDecoder().decode(tokens[1])));
  }
}
