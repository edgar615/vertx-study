package com.edgar.vertx.web.pebble;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

public class StaticServer extends AbstractVerticle {


    @Override
    public void start() throws Exception {
//   不经过后端渲染，全部通过前端操作，这个其实是handlebars的学习
//http://javascriptissexy.com/handlebars-js-tutorial-learn-everything-about-handlebars-js-javascript-templating
      Router router = Router.router(vertx);
      router.route("/static/*").handler(StaticHandler.create());
        vertx.createHttpServer().requestHandler(router::accept).listen(9000);
    }
}