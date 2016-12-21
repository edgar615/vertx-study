package com.edgar.vertx.serviceproxy.provider;

import com.edgar.vertx.serviceproxy.provider.impl.ProcessorServiceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.serviceproxy.ProxyHelper;

public class ProcessorServiceVerticle extends AbstractVerticle {

  ProcessorService service;

  @Override
  public void start() throws Exception {
    // Create the client object
    service = new ProcessorServiceImpl();
    // Register the handler
    ProxyHelper.registerService(ProcessorService.class, vertx, service, "vertx.processor");

    //
//    Router router = Router.router(vertx);
//
//    // Allow events for the designated addresses in/out of the event bus bridge
//    BridgeOptions opts = new BridgeOptions()
//        .addInboundPermitted(new PermittedOptions().setAddress("vertx.processor"))
//        .addOutboundPermitted(new PermittedOptions().setAddress("vertx.processor"));
//
//    // Create the event bus bridge and add it to the router.
//    SockJSHandler ebHandler = SockJSHandler.create(vertx).bridge(opts);
//    router.route("/eventbus/*").handler(ebHandler);
//
//    //
//    router.route().handler(StaticHandler.create());
//
//    //
//    vertx.createHttpServer().requestHandler(router::accept).listen(8080);

  }

}