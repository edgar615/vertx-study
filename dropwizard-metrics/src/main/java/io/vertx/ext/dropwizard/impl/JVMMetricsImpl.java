package io.vertx.ext.dropwizard.impl;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.metrics.Metrics;
import io.vertx.ext.dropwizard.impl.AbstractMetrics;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by Edgar on 2017/3/7.
 *
 * @author Edgar  Date 2017/3/7
 */
public class JVMMetricsImpl implements Metrics {
  private MetricRegistry registry;
  private String baseName;
  public JVMMetricsImpl(MetricRegistry registry, String baseName) {
    this.registry = registry;
    this.baseName = baseName;
    registry.register(MetricRegistry.name(baseName, "jvm", "memory"),
                     new MemoryUsageGaugeSet());
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public void close() {

  }

  public JsonObject metrics() {
    Map<String, Object> map = registry.getMetrics().
            entrySet().
            stream().
            filter(e -> e.getKey().startsWith(baseName)).
            collect(Collectors.toMap(
                    e -> e.getKey(),
                    e -> Helper.convertMetric(e.getValue(), TimeUnit.SECONDS, TimeUnit.MILLISECONDS)));
    return new JsonObject(map);
  }
}
