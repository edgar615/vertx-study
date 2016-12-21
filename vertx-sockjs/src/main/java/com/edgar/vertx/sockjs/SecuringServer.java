package com.edgar.vertx.sockjs;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.jwt.impl.JWTUser;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BasicAuthHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeEventType;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

/**
 * Created by Edgar on 2016/12/21.
 *
 * @author Edgar  Date 2016/12/21
 */
public class SecuringServer extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(SecuringServer.class.getName());
  }

  @Override
  public void start() throws Exception {

    Router router = Router.router(vertx);
    vertx.eventBus().consumer("demo.orderMgr", msg -> {
      System.out.println(msg.body());
    });
    vertx.eventBus().consumer("demo.persistor", msg -> {
      System.out.println(msg.body());
    });
    // Allow outbound traffic to the news-feed address

    // Let through any messages sent to 'demo.orderMgr' from the client
    PermittedOptions inboundPermitted1 = new PermittedOptions().setAddress("demo.orderMgr");

// Allow calls to the address 'demo.persistor' from the client as long as the messages
// have an action field with value 'find' and a collection field with value
// 'albums'
    PermittedOptions inboundPermitted2 = new PermittedOptions().setAddress("demo.persistor")
            .setMatch(new JsonObject().put("action", "find")
                              .put("collection", "albums"));

// Allow through any message with a field `wibble` with value `foo`.
    PermittedOptions inboundPermitted3 = new PermittedOptions().setMatch(new JsonObject().put("wibble", "foo"));

// First let's define what we're going to allow from server -> client

// Let through any messages coming from address 'ticker.mystock'
    PermittedOptions outboundPermitted1 = new PermittedOptions().setAddress("ticker.mystock");
    //setRequiredAuthority("place_orders");;

// Let through any messages from addresses starting with "news." (e.g. news.europe, news.usa, etc)
    PermittedOptions outboundPermitted2 = new PermittedOptions().setAddressRegex("news\\..+")
            .setMatch(new JsonObject().put("action", "update"));

// Let's define what we're going to allow from client -> server
    BridgeOptions options = new BridgeOptions().
            addInboundPermitted(inboundPermitted1).
            addInboundPermitted(inboundPermitted2).
            addInboundPermitted(inboundPermitted3).
            addOutboundPermitted(outboundPermitted1).
            addOutboundPermitted(outboundPermitted2);

//    router.route("/eventbus/info").handler(BasicAuthHandler.create(new AuthProvider() {
//      @Override
//      public void authenticate(JsonObject jsonObject, Handler<AsyncResult<User>> handler) {
//        handler.handle(Future.succeededFuture(new JWTUser(new JsonObject(), "a")));
//      }
//    }));
    router.route("/eventbus/*").handler(SockJSHandler.create(vertx).bridge(options));

    // Serve the static resources
    router.route().handler(StaticHandler.create());

    vertx.createHttpServer().requestHandler(router::accept).listen(8080);

    // Publish a message to the address "ticker.mystock every second //accept
    vertx.setPeriodic(1000, t -> vertx.eventBus().publish("ticker.mystock", "ticker from the server!"));
    // Publish a message to the address "news-feed every second //deny
    vertx.setPeriodic(1500, t -> vertx.eventBus().publish("news-feed", "news-feed from the "
                                                                       + "server!"));
    // Publish a message to the address "news-feed every second //deny
    vertx.setPeriodic(2000, t -> vertx.eventBus().publish("news.feed", "news.feed from the "
                                                                       + "server!"));
    //deny
    vertx.setPeriodic(3000, t -> vertx.eventBus().publish("news.feed",new JsonObject().put
            ("action", "insert")));

    //accept
    vertx.setPeriodic(3000, t -> vertx.eventBus().publish("news.feed",new JsonObject().put
            ("action", "update")));

    //accept
    vertx.setPeriodic(3000, t -> vertx.eventBus().publish("news.feed",new JsonObject().put
            ("action", "update").put("foo", "bar")));
    // Publish a message to the address "some-feed" every second //deny
//    vertx.setPeriodic(1000, t -> vertx.eventBus().publish("some-feed", "news from the server!"));
  }
}
