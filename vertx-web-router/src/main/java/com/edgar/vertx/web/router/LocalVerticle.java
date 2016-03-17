package com.edgar.vertx.web.router;

import com.edgar.vertx.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Locale;
import io.vertx.ext.web.Router;

/**
 * Created by Edgar on 2016/3/17.
 *
 * @author Edgar  Date 2016/3/17
 */
public class LocalVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(LocalVerticle.class);
  }

  @Override
  public void start() throws Exception {
    HttpServer server = vertx.createHttpServer();

    //Vert.x Web parses the Accept-Language header and provides some helper methods to identify
    // which is the preferred locale for a client or the sorted list of preferred locales by
    // quality.
    Router router = Router.router(vertx);
    router.get("/localized").handler(rc -> {
      // although it might seem strange by running a loop with a switch we
      // make sure that the locale order of preference is preserved when
      // replying in the users language.
      for (Locale locale : rc.acceptableLocales()) {
        switch (locale.language()) {
          case "en":
            rc.response().end("Hello!");
            return;
          case "fr":
            rc.response().end("Bonjour!");
            return;
          case "pt":
            rc.response().end("Olá!");
            return;
          case "es":
            rc.response().end("Hola!");
            return;
        }
      }
      // we do not know the user language so lets just inform that back:
      rc.response().end("Sorry we don't speak: " + rc.preferredLocale());
    });
    server.requestHandler(router::accept).listen(8080);
  }
}
