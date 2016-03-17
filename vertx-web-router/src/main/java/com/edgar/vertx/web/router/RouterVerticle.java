package com.edgar.vertx.web.router;

import com.edgar.vertx.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

/**
 * Created by Edgar on 2016/3/17.
 *
 * @author Edgar  Date 2016/3/17
 */
public class RouterVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(RouterVerticle.class);
  }

  @Override
  public void start() throws Exception {
    HttpServer server = vertx.createHttpServer();

    Router router = Router.router(vertx);

    router.route("/foo").handler(rc -> {
      // This handler will be called for every request
      HttpServerResponse response = rc.response();
      response.putHeader("content-type", "text/plain");

      // Write to the response and end it
      response.end(rc.normalisedPath());
    });

//    Often you want to route all requests that begin with a certain path. You could use a regex
// to do this, but a simply way is to use an asterisk * at the end of the path when declaring the
// route path.
    router.route("/some/path/*").handler(rc -> {
      HttpServerResponse response = rc.response();
      response.putHeader("content-type", "text/plain");

      response.end(rc.normalisedPath());
    });

    //It’s possible to match paths using placeholders for parameters which are then available in
    // the request params.
    router.route(HttpMethod.GET, "/catalogue/products/:producttype/:productid/")
            .handler(rc -> {
              String productType = rc.request().getParam("producttype");
              String productID = rc.request().getParam("productid");
              HttpServerResponse response = rc.response();
              response.putHeader("content-type", "text/plain");

              // Write to the response and end it
              response.end("producttype : " + productType + ",productid : " + productID);
            });

    //Regular expressions can also be used to match URI paths in routes.
    router.routeWithRegex(".*foo").handler(rc -> {
      HttpServerResponse response = rc.response();
      response.putHeader("content-type", "text/plain");

      response.end(rc.normalisedPath());
    });
    //You can also capture path parameters when using regular expressions
// This regular expression matches paths that start with something like:
// "/bar/123" - where the "bar" is captured into param0 and the "123" is captured into
// param1
    router.routeWithRegex(".*bar").pathRegex("\\/([^\\/]+)\\/([^\\/]+)").handler(routingContext -> {

      String productType = routingContext.request().getParam("param0");
      String productID = routingContext.request().getParam("param1");

      routingContext.response().end("producttype : " + productType + ",productid : " + productID);
    });

//    You can specify that a route will match against matching request MIME types using consumes.
//            In this case, the request will contain a content-type header specifying the MIME
// type of the request body. This will be matched against the value specified in consumes.
//    Basically, consumes is describing which MIME types the handler can consume.
    router.route("/consume").consumes("text/html").consumes("text/plain")
            .handler(routingContext -> {
              HttpServerResponse response = routingContext.response();
              response.putHeader("content-type", "text/plain");

              // Write to the response and end it
              response.end(routingContext.normalisedPath());

            });

    router.route("/text").consumes("text/*").handler(routingContext -> {
      routingContext.response().end(routingContext.normalisedPath());
    });

    router.route("/text").consumes("*/json").handler(routingContext -> {
      routingContext.response().end(routingContext.normalisedPath());
    });


    //The HTTP accept header is used to signify which MIME types of the response are acceptable
    // to the client.
    //An accept header can have multiple MIME types separated by ‘,’.
    //MIME types can also have a q value appended to them* which signifies a weighting to apply
    // if more than one response MIME type is available matching the accept header. The q value
    // is a number between 0 and 1.0. If omitted it defaults to 1.0.

    //Accept: text/plain
    //Accept: text/plain, text/html
    //Accept: text/plain; q=0.9, text/html With the following the client will accept text/plain
    // or text/html but prefers text/html as it has a higher q value (the default value is q=1.0)

    ///    In this case the route will match with any request with an accept header that matches
    // application/json.
//    Accept: application/json
//    Accept: application/*
//Accept: application/json, text/html
//Accept: application/json;q=0.7, text/html;q=0.8, text/plain

    router.route("/json").produces("application/json").handler(routingContext -> {

      HttpServerResponse response = routingContext.response();
      response.putHeader("content-type", "application/json");
      response.setChunked(true);
      response.write(new JsonObject().put("foo", "bar").encode()).end();

    });

    //You can also mark your route as producing more than one MIME type. If this is the case,
    // then you use getAcceptableContentType to find out the actual MIME type that was accepted.
    router.route("/json2").produces("application/json").produces("text/html").handler
            (routingContext -> {

              HttpServerResponse response = routingContext.response();

              // Get the actual MIME type acceptable
              String acceptableContentType = routingContext.getAcceptableContentType();

              response.putHeader("content-type", acceptableContentType);
              response.setChunked(true);
              response.write(new JsonObject().put("foo", "bar").encode()).end();
            });

    //Default 404 Handling
    router.route().handler(routingContext -> {

      // This handler will be called for every request
      HttpServerResponse response = routingContext.response();
      response.putHeader("content-type", "text/plain");

      // Write to the response and end it
      response.end("Hello World from Vert.x-Web!");
    });

    server.requestHandler(router::accept).listen(8080);
  }
}
