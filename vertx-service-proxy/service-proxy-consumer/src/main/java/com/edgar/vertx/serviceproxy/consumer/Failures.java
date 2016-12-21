package com.edgar.vertx.serviceproxy.consumer;

import com.edgar.vertx.serviceproxy.provider.ProcessorService;
import io.vertx.serviceproxy.ServiceException;

public class Failures {
  public static void dealWithFailure(Throwable t) {
    if (t instanceof ServiceException) {
      ServiceException exc = (ServiceException) t;
      if (exc.failureCode() == ProcessorService.BAD_NAME_ERROR) {
        System.out.println("Failed to process the document: The name in the document is bad. " +
            "The name provided is: " + exc.getDebugInfo().getString("name"));
      } else if (exc.failureCode() == ProcessorService.NO_NAME_ERROR) {
        System.out.println("Failed to process the document: No name was found");
      }
    } else {
      System.out.println("Unexpected error: " + t);
    }
  }
}