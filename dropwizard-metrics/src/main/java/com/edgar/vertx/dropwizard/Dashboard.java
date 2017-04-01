package com.edgar.vertx.dropwizard;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.metrics.MetricsOptions;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import io.vertx.ext.dropwizard.Match;
import io.vertx.ext.dropwizard.MatchType;
import io.vertx.ext.dropwizard.MetricsService;
import io.vertx.ext.dropwizard.impl.JVMMetricsImpl;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

import java.util.Random;
import java.util.Set;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Dashboard extends AbstractVerticle {
  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
//    Vertx.vertx(new VertxOptions()
//                        .setMetricsOptions(new MetricsOptions(new JsonObject().put
//                                ("registryName", "my-registry")).setEnabled(true)))
    MetricsOptions metricsOptions = new DropwizardMetricsOptions()
            .setRegistryName("my-registry")
            .setEnabled(true)
            .addMonitoredHttpServerUri(
                    new Match().setValue("/")).
                    addMonitoredHttpServerUri(
                            new Match().setValue("/foo/.*").setType(
                                    MatchType.REGEX));
    Vertx.vertx(new VertxOptions().setMetricsOptions(metricsOptions))
    .deployVerticle(Dashboard.class.getName());

  }

  @Override
  public void start() {

    MetricsService service = MetricsService.create(vertx);

    Router router = Router.router(vertx);

    // Allow outbound traffic to the news-feed address

    BridgeOptions options = new BridgeOptions().
            addOutboundPermitted(
                    new PermittedOptions().
                            setAddress("metrics")
            );

    router.route("/eventbus/*").handler(SockJSHandler.create(vertx).bridge(options));

    // Serve the static resources
    router.route().handler(StaticHandler.create());

    HttpServer httpServer = vertx.createHttpServer();
    httpServer.requestHandler(router::accept).listen(8080);

    MetricRegistry registry = SharedMetricRegistries.getOrCreate("my-registry");

    JVMMetricsImpl jvmMetrics = new JVMMetricsImpl(registry, "vertx");

    // Send a metrics events every second
    vertx.setPeriodic(1000, t -> {
      JsonObject metrics = service.getMetricsSnapshot(vertx.eventBus());
      vertx.eventBus().publish("metrics", metrics);
//      JsonObject metrics2 = service.getMetricsSnapshot("vertx.eventbus.messages.received");
//      System.out.println(metrics2);
      JsonObject metrics2 = service.getMetricsSnapshot("vertx.jvm.memory");
      System.out.println(metrics2);
//      Set<String> metricsNames = service.metricsNames();
//      for (String metricsName : metricsNames) {
//        System.out.println("Known metrics name " + metricsName);
//      }
    });

    // Send some messages
    Random random = new Random();
    vertx.eventBus().consumer("whatever", msg -> {
      vertx.setTimer(10 + random.nextInt(50), id -> {
        vertx.eventBus().send("whatever", "hello");
      });
    });
//    vertx.eventBus().send("whatever", "hello");
  }

}