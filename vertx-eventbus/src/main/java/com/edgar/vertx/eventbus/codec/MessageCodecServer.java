package com.edgar.vertx.eventbus.codec;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;

/**
 * Created by Administrator on 2015/8/31.
 */
public class MessageCodecServer {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    EventBus eb = vertx.eventBus();

    MyCodec myCodec = new MyCodec();
    eb.registerCodec(myCodec);

    DeliveryOptions options = new DeliveryOptions().setCodecName(myCodec.name());

    eb.consumer("news.uk.sport", message -> {
      System.out.println("I have received a message: " + message.body());
    });

    MessageConsumer<MyCodec> consumer = eb.consumer("news.uk.sport");
    consumer.handler(message -> {
      System.out.println("I also have received a message: " + message.body());
    });

    //Publishing messages
    //That message will then be delivered to all handlers registered against the address news.uk
    // .sport.
    eb.send("news.uk.sport", new MyPojo("Yay! Someone kicked a ball"), options);
    eb.send("news.uk.sport", new MyPojo("2Yay! Someone kicked a ball"), options);
    eb.send("news.uk.sport", new MyPojo("3Yay! Someone kicked a ball"), options);

  }
}