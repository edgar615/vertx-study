package com.edgar.vertx.kafka;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.kafka.client.common.TopicPartition;
import io.vertx.kafka.client.consumer.KafkaConsumer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by edgar on 17-3-11.
 */
public class ConsumerStartVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(ConsumerStartVerticle.class.getName());
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

    consumer.handler(record -> {
      System.out.println("Processing key=" + record.key() + ",value=" + record.value() +
          ",partition=" + record.partition() + ",offset=" + record.offset());
    });

    // registering handlers for assigned and revoked partitions
    consumer.partitionsAssignedHandler(topicPartitions -> {

      System.out.println("Partitions assigned");
      for (TopicPartition topicPartition : topicPartitions) {
        System.out.println(topicPartition.getTopic() + " " + topicPartition.getPartition());
      }
    });

    consumer.partitionsRevokedHandler(topicPartitions -> {

      System.out.println("Partitions revoked");
      for (TopicPartition topicPartition : topicPartitions) {
        System.out.println(topicPartition.getTopic() + " " + topicPartition.getPartition());
      }
    });

    consumer.subscribe("test", ar -> {
      if (ar.succeeded()) {
        System.out.println("subscribed");
      } else {
        System.out.println("Could not subscribe " + ar.cause().getMessage());
      }
    });
  }
}
