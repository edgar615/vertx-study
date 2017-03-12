package com.edgar.vertx.kafka;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.kafka.client.common.PartitionInfo;
import io.vertx.kafka.client.consumer.KafkaConsumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by edgar on 17-3-11.
 */
public class ListTopicVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(ListTopicVerticle.class.getName());
  }


  @Override
  public void start() throws Exception {
    Map<String, String> config = new HashMap<>();
    config.put("bootstrap.servers", "localhost:9092");
    config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    config.put("group.id", "my_group");
    config.put("auto.offset.reset", "earliest");
    config.put("enable.auto.commit", "false");

// use consumer for interacting with Apache Kafka
    KafkaConsumer<String, String> consumer = KafkaConsumer.create(vertx, config);
    consumer.listTopics(ar -> {

      if (ar.succeeded()) {

        Map<String, List<PartitionInfo>> map = ar.result();
        map.forEach((topic, partitions) -> {
          System.out.println("topic = " + topic);
          System.out.println("partitions = " + map.get(topic));
        });
      }
    });
  }
}
