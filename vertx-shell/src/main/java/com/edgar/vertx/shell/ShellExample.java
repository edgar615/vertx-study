package com.edgar.vertx.shell;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Launcher;
import io.vertx.core.Vertx;
import io.vertx.core.cli.Argument;
import io.vertx.core.cli.CLI;
import io.vertx.core.cli.CommandLine;
import io.vertx.core.cli.Option;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.shell.ShellService;
import io.vertx.ext.shell.ShellServiceOptions;
import io.vertx.ext.shell.ShellVerticle;
import io.vertx.ext.shell.command.CommandBuilder;
import io.vertx.ext.shell.command.CommandRegistry;
import io.vertx.ext.shell.term.HttpTermOptions;
import io.vertx.ext.shell.term.TelnetTermOptions;

/**
 * Created by Edgar on 2017/6/16.
 *
 * @author Edgar  Date 2017/6/16
 */
public class ShellExample extends AbstractVerticle {

  public static void main(String[] args) {
//    Vertx.vertx().deployVerticle(ShellVerticle.class.getName(),
//                                 new DeploymentOptions().setConfig(
//                                         new JsonObject().put("telnetOptions",
//                                                              new JsonObject().
//                                                                      put("host", "localhost").
//                                                                      put("port", 4000))
//                                 )
//    );

//    Vertx.vertx().deployVerticle(ShellExample.class.getName());
    new Launcher().execute("run", ShellExample.class.getName());
  }

  @Override
  public void start() throws Exception {
    //支持telnet，ssh, http三种方式
//    http://localhost:4000/shell.html
    ShellService service = ShellService.create(vertx,
                                               new ShellServiceOptions().setHttpOptions(
                                                       new HttpTermOptions().
                                                               setHost("localhost").
                                                               setPort(4000)
                                               ).setWelcomeMessage("hello")
    );
    service.start();

    //自定义命令1
    CommandBuilder builder = CommandBuilder.command("my-command");
    builder.processHandler(process -> {

      // Write a message to the console
      process.write("Hello World\n");
      for (String arg : process.args()) {
        // Print each argument on the console
        process.write("Argument " + arg);
        process.write("\n");
      }
      // End the process
      process.end();
    });

// Register the command
    CommandRegistry registry = CommandRegistry.getShared(vertx);
    registry.registerCommand(builder.build(vertx));

    //自定义命令2 my-command2 1 -m 3
    CLI cli = CLI.create("my-command2").
            addArgument(new Argument().setArgName("my-arg")).
            addOption(new Option().setShortName("m").setLongName("my-option"));
    CommandBuilder command = CommandBuilder.command(cli);
    registry.registerCommand(command.build(vertx));
    command.processHandler(process -> {

      CommandLine commandLine = process.commandLine();

      String argValue = commandLine.getArgumentValue(0);
      String optValue = commandLine.getOptionValue("my-option");
      process.write("The argument is " + argValue + " and the option is " + optValue);

      process.end();
    });

    //自定义命令1
    builder = CommandBuilder.command("my-command3");
    registry.registerCommand(builder.build(vertx));
    builder.processHandler(process -> {

      // Write a message to the console
      process.write("Hello World\n");
      for (String arg : process.args()) {
        // Print each argument on the console
        process.write("Argument " + arg);
        process.write("\n");
      }
      process.write("Current terminal size: (" + process.width() + ", " + process.height() + ")");
      process.stdinHandler(data -> {
        System.out.println("Received " + data);
//        process.write("Received " + data);
      });
      // Every second print a message on the console
      long periodicId = vertx.setPeriodic(1000, id -> {
        process.write("tick\n");
      });

      // When user press Ctrl+C: cancel the timer and end the process
      process.interruptHandler(v -> {
        vertx.cancelTimer(periodicId);
        process.end();
      });
//      process.end();
    });
  }
}
