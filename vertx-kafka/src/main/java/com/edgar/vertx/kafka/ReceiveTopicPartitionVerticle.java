package com.edgar.vertx.kafka;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.kafka.client.common.TopicPartition;
import io.vertx.kafka.client.consumer.KafkaConsumer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by edgar on 17-3-11.
 */
public class ReceiveTopicPartitionVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(ReceiveTopicPartitionVerticle.class.getName());
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
    Set<TopicPartition> topicPartitions = new HashSet<>();
    topicPartitions.add(new TopicPartition()
        .setTopic("test")
        .setPartition(0));

// requesting to be assigned the specific partition
    consumer.assign(topicPartitions, done -> {

      if (done.succeeded()) {
        System.out.println("Partition assigned");

        // requesting the assigned partitions
        consumer.assignment(done1 -> {

          if (done1.succeeded()) {

            for (TopicPartition topicPartition : done1.result()) {
              System.out.println(topicPartition.getTopic() + " " + topicPartition.getPartition());
            }
          }
        });
      }
    });
  }
}
