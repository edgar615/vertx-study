package com.edgar.vertx.sockjs;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeEventType;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;

/**
 * Created by Edgar on 2016/12/21.
 *
 * @author Edgar  Date 2016/12/21
 */
public class EbServer extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(EbServer.class.getName());
  }

  @Override
  public void start() throws Exception {

    Router router = Router.router(vertx);

    vertx.eventBus().consumer("client-msg", msg -> {
      System.out.println(msg.body());
      msg.fail(-1, "err");
//      msg.reply("pong");
    });

    // Allow outbound traffic to the news-feed address

    BridgeOptions
            options = new BridgeOptions().addOutboundPermitted(new PermittedOptions().setAddress("news-feed"))
            .addInboundPermitted(new PermittedOptions().setAddress("client-msg"));
    router.route("/eventbus/*").handler(SockJSHandler.create(vertx).bridge(options, event -> {
      // You can also optionally provide a handler like this which will be passed any events that occur on the bridge
      // You can use this for monitoring or logging, or to change the raw messages in-flight.
      // It can also be used for fine grained access control.

      if (event.type() == BridgeEventType.SOCKET_CREATED) {
        System.out.println("A socket was created, options:" + event.getRawMessage());
//        event.socket().end(Buffer.buffer(new JsonObject().put("foo", "bar").encode()));
      }
      if (event.type() == BridgeEventType.PUBLISH) {
        System.out.println(event.getRawMessage());
      }
      if (event.type() == BridgeEventType.SEND) {
        System.out.println(event.getRawMessage());
      }
      // This signals that it's ok to process the event
      event.complete(true);

    }));
//    router.route("/eventbus/*").handler(SockJSHandler.create(vertx).bridge(options));
    // Serve the static resources
    router.route().handler(StaticHandler.create());

    vertx.createHttpServer().requestHandler(router::accept).listen(8080);

    // Publish a message to the address "news-feed" every second
    vertx.setPeriodic(1000, t -> vertx.eventBus().publish("news-feed", "news from the server!"));
  }
}
