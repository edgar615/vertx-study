package com.edgar.vertx.dns;

import com.edgar.util.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.dns.DnsClient;
import io.vertx.core.dns.MxRecord;

import java.util.List;

/**
 * Created by Edgar on 2016/3/16.
 *
 * @author Edgar  Date 2016/3/16
 */
public class DnsExample extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(DnsExample.class);
  }

  @Override
  public void start() throws Exception {
    DnsClient dnsClient = vertx.createDnsClient(53, "10.0.0.1");

//    lookup
//
//    Try to lookup the A (ipv4) or AAAA (ipv6) record for a given name. The first which is returned will be used, so it behaves the same way as you may be used from when using "nslookup" on your operation system.
//
//    To lookup the A / AAAA record for "vertx.io" you would typically use it like:

    dnsClient.lookup("vertx.io", ar -> {
      if (ar.succeeded()) {
        System.out.println(ar.result());
      } else {
        System.out.println("Failed to resolve entry" + ar.cause());
      }
    });

    dnsClient.lookup4("vertx.io", ar -> {
      if (ar.succeeded()) {
        System.out.println(ar.result());
      } else {
        System.out.println("Failed to resolve entry" + ar.cause());
      }
    });

    dnsClient.lookup6("vertx.io", ar -> {
      if (ar.succeeded()) {
        System.out.println(ar.result());
      } else {
        System.out.println("Failed to resolve entry" + ar.cause());
      }
    });

    //Try to resolve all A (ipv4) records for a given name. This is quite similar to using "dig" on unix like operation systems.
    dnsClient.resolveA("vertx.io", ar -> {
      if (ar.succeeded()) {
        List<String> records = ar.result();
        for (String record : records) {
          System.out.println(record);
        }
      } else {
        System.out.println("Failed to resolve entry" + ar.cause());
      }
    });

    //Try to resolve all AAAA (ipv6) records for a given name. This is quite similar to using "dig" on unix like operation systems.
    dnsClient.resolveAAAA("vertx.io", ar -> {
      if (ar.succeeded()) {
        List<String> records = ar.result();
        for (String record : records) {
          System.out.println(record);
        }
      } else {
        System.out.println("Failed to resolve entry" + ar.cause());
      }
    });

    //Try to resolve all CNAME records for a given name. This is quite similar to using "dig" on unix like operation systems.
    dnsClient.resolveCNAME("vertx.io", ar -> {
      if (ar.succeeded()) {
        List<String> records = ar.result();
        for (String record : records) {
          System.out.println(record);
        }
      } else {
        System.out.println("Failed to resolve entry" + ar.cause());
      }
    });

    //Try to resolve all MX records for a given name. The MX records are used to define which Mail-Server accepts emails for a given domain.
    dnsClient.resolveMX("vertx.io", ar -> {
      if (ar.succeeded()) {
        List<MxRecord> records = ar.result();
        for (MxRecord record: records) {
          System.out.println(record);
        }
      } else {
        System.out.println("Failed to resolve entry" + ar.cause());
      }
    });

    //Try to resolve all TXT records for a given name. TXT records are often used to define extra informations for a domain.
//    resolveTXT
    //resolveNS
    //resolveSRV
    //resolvePTR
    //reverseLookup
  }
}
