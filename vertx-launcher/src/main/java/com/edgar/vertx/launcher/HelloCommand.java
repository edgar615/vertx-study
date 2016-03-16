package com.edgar.vertx.launcher;

import io.vertx.core.cli.CLIException;
import io.vertx.core.cli.annotations.Name;
import io.vertx.core.cli.annotations.Option;
import io.vertx.core.cli.annotations.Summary;
import io.vertx.core.spi.launcher.DefaultCommand;

@Name("my-command")
@Summary("A simple hello command.")
public class HelloCommand extends DefaultCommand {

  private String name;

  @Option(longName = "name", required = true)
  public void setName(String n) {
    this.name = n;
  }


//  To use the Launcher class in a fat-jar just set the Main-Class of the MANIFEST to io.vertx
// .core.Launcher. In addition, set the Main-Verticle MANIFEST entry to the name of your main
// verticle.
//
//  By default, it executed the run command. However, you can configure the default command by
// setting the Main-Command MANIFEST entry. The default command is used if the fat jar is
// launched without a command.

//  You can also create a sub-class of Launcher to start your application. The class has been designed to be easily extensible.
//
//  A Launcher sub-class can:
//
//  customize the vert.x configuration in beforeStartingVertx
//
//  retrieve the vert.x instance created by the "run" or "bare" command by overriding afterStartingVertx
//
//  configure the default verticle and command with getMainVerticle and getDefaultCommand
//
//  add / remove commands using register and unregister

  @Override
  public void run() throws CLIException {
    System.out.println("Hello " + name);
  }
}