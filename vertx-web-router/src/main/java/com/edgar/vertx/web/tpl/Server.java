package com.edgar.vertx.web.tpl;

import com.edgar.vertx.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.ErrorHandler;
import io.vertx.ext.web.templ.TemplateEngine;

public class Server extends AbstractVerticle {

    public static void main(String[] args) {
        Runner.runExample(Server.class);
    }

    @Override
    public void start() throws Exception {
//      Template engines are described by TemplateEngine. In order to render a template render is used.
//
//              The simplest way to use templates is not to call the template engine directly but to use the TemplateHandler. This handler calls the template engine for you based on the path in the HTTP request.
//
//              By default the template handler will look for templates in a directory called templates. This can be configured.

//      TemplateEngine engine = HandlebarsTemplateEngine.create();
//
        Router router = Router.router(vertx);
//        router.get().handler(rc -> {
//           rc.put("name", "vert.x web");
//
//            engine.render(rc, "templates/index.hbs", res -> {
//                if (res.succeeded()) {
//                    rc.response().end(res.result());
//                } else {
//                    rc.fail(res.cause());
//                }
//            });
//        });
        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }
}