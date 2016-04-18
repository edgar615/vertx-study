package com.edgar.vertx.web.auth;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.AuthHandler;
import io.vertx.ext.web.handler.BasicAuthHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.UserSessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;


/**
 * Created by Edgar on 2016/3/17.
 *
 * @author Edgar  Date 2016/3/17
 */
public class BasicAuthVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(BasicAuthVerticle.class);
  }

  @Override
  public void start() throws Exception {
    Router router = Router.router(vertx);
    router.route().handler(CookieHandler.create());
    router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));

//    Creating an auth handler
//
//    To create an auth handler you need an instance of AuthProvider. Auth provider is used for
// authentication and authorisation of users. Vert.x provides several auth provider instances out
// of the box in the vertx-auth project. For full information on auth providers and how to use
// and configure them please consult the auth documentation.
    AuthProvider authProvider = null;

    router.route().handler(UserSessionHandler.create(authProvider));

    AuthHandler basicAuthHandler = BasicAuthHandler.create(authProvider);

// All requests to paths starting with '/private/' will be protected
    router.route("/private/*").handler(basicAuthHandler);

    router.route("/someotherpath").handler(routingContext -> {

      // This will be public access - no login required

    });

    router.route("/private/somepath").handler(routingContext -> {

      // This will require a login

      // This will have the value true
      boolean isAuthenticated = routingContext.user() != null;

    });
  }
}
