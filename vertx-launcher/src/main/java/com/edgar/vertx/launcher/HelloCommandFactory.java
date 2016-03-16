package com.edgar.vertx.launcher;

import io.vertx.core.spi.launcher.DefaultCommandFactory;

public class HelloCommandFactory extends DefaultCommandFactory<HelloCommand> {
  public HelloCommandFactory() {
    super(HelloCommand.class);
  }
}