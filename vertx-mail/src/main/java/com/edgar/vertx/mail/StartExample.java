package com.edgar.vertx.mail;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailConfig;
import io.vertx.ext.mail.MailMessage;
import io.vertx.ext.mail.StartTLSOptions;

/**
 * Created by Edgar on 2017/11/30.
 *
 * @author Edgar  Date 2017/11/30
 */
public class StartExample extends AbstractVerticle {
  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(StartExample.class.getName());
  }

  @Override
  public void start() throws Exception {
    MailConfig config = new MailConfig();
    MailClient mailClient = MailClient.createShared(vertx, config, "exampleclient");
    config.setHostname("xxxx.com");
//    config.setPort(587);
    config.setStarttls(StartTLSOptions.REQUIRED);
    config.setUsername("xxxx");
    config.setPassword("xxxx");

    MailMessage message = new MailMessage();
    message.setFrom("xxx@126.com");
    message.setTo("xxx@xxx.com");
//    message.setCc("Another User <another@example.net>");
    message.setText("this is the plain message text");
    message.setHtml("this is html text <a href=\"http://vertx.io\">vertx.io</a>");

    //附件
//    MailAttachment attachment = new MailAttachment();
//    attachment.setContentType("text/plain");
//    attachment.setData(Buffer.buffer("attachment file"));
//
//    message.setAttachment(attachment);

    //内联附件
//    MailAttachment attachment = new MailAttachment();
//    attachment.setContentType("image/jpeg");
//    attachment.setData(Buffer.buffer("image data"));
//    attachment.setDisposition("inline");
//    attachment.setContentId("<image1@example.com>");
//
//    message.setInlineAttachment(attachment);

    mailClient.sendMail(message, result -> {
      if (result.succeeded()) {
        System.out.println(result.result());
      } else {
        result.cause().printStackTrace();
      }
    });
  }
}
