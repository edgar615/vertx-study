package com.edgar.vertx.kafka.dashboard;

import com.sun.management.OperatingSystemMXBean;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.producer.KafkaWriteStream;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MetricsVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(MetricsVerticle.class.getName());
  }


  private OperatingSystemMXBean systemMBean;
  private KafkaWriteStream<String, JsonObject> producer;

  @Override
  public void start() throws Exception {
    systemMBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

    // A random identifier
    String pid = UUID.randomUUID().toString();

//    Get the kafka producer config
    Map<String, Object> config = new HashMap<>();
    config.put("bootstrap.servers", "localhost:9092");
    config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    config.put("group.id", "my_group");
    config.put("auto.offset.reset", "earliest");
    config.put("enable.auto.commit", "false");

    // Create the producer
    producer = KafkaWriteStream.create(vertx, config, String.class, JsonObject.class);


//    producer = KafkaWriteStream.create(vertx, config().getMap(), String.class, JsonObject.class);

    // Publish the metircs in Kafka
    vertx.setPeriodic(1000, id -> {
      JsonObject metrics = new JsonObject();
      metrics.put("CPU", systemMBean.getProcessCpuLoad());
      metrics.put("Mem", systemMBean.getTotalPhysicalMemorySize() - systemMBean.getFreePhysicalMemorySize());
      producer.write(new ProducerRecord<>("the_topic", new JsonObject().put(pid, metrics)));
    });
  }

  @Override
  public void stop() throws Exception {
    if (producer != null) {
      producer.close();
    }
  }
}