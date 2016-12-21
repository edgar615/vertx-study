package com.edgar.vertx.sockjs;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;

/**
 * Created by Edgar on 2016/12/21.
 *
 * @author Edgar  Date 2016/12/21
 */
public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(Server.class.getName());
  }

  @Override
  public void start() throws Exception {
    Router router = Router.router(vertx);
    router.route().handler(CorsHandler.create("*").allowedMethod(
            HttpMethod.GET).allowedMethod(HttpMethod.POST).allowedMethod(HttpMethod.OPTIONS)
                                   .allowedMethod(HttpMethod.PUT).allowedMethod(HttpMethod.DELETE)
                                   .allowedHeader("content-type").allowedHeader("Authorization"));

    SockJSHandlerOptions options = new SockJSHandlerOptions()
            .setHeartbeatInterval(2000l);
    SockJSHandler sockJSHandler = SockJSHandler.create(vertx, options);
    sockJSHandler.socketHandler(sockJSSocket -> {
      // Just echo the data back
      sockJSSocket.handler(sockJSSocket::write);
    });

    router.route("/myapp/*").handler(sockJSHandler);
    router.route("/webroot/*").handler(StaticHandler.create());

    vertx.createHttpServer().requestHandler(router::accept)
            .listen(8080);
  }
}
