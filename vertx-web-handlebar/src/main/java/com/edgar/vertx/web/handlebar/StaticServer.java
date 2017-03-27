package com.edgar.vertx.web.handlebar;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.templ.HandlebarsTemplateEngine;
import io.vertx.ext.web.templ.TemplateEngine;

public class StaticServer extends AbstractVerticle {

    public static void main(String[] args) {
        Runner.runExample(StaticServer.class);
    }

    @Override
    public void start() throws Exception {
//   不经过后端渲染，全部通过前端操作，这个其实是handlebars的学习
//http://javascriptissexy.com/handlebars-js-tutorial-learn-everything-about-handlebars-js-javascript-templating
      Router router = Router.router(vertx);
      router.route("/static/*").handler(StaticHandler.create());
        vertx.createHttpServer().requestHandler(router::accept).listen(9000);
    }
}