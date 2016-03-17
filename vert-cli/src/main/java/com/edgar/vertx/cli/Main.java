package com.edgar.vertx.cli;

import io.vertx.core.cli.Argument;
import io.vertx.core.cli.CLI;
import io.vertx.core.cli.CommandLine;
import io.vertx.core.cli.Option;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edgar on 2016/3/17.
 *
 * @author Edgar  Date 2016/3/17
 */
public class Main {
  public static void main(String[] args) {
    CLI cli = CLI.create("copy")
            .addOption(new Option()
                               .setLongName("mandatory")
                               .setRequired(true)
                               .setDescription("a mandatory option"))
            .addOption(new Option()
                               .setLongName("flag").setShortName("f").setFlag(true)
                               .setDescription("a flag"))
            .addOption(new Option()
                               .setLongName("multiple").setShortName("m").setMultiValued(true)
                               .setDescription("a multi-valued option"))
            .addOption(new Option()
                               .setLongName("optional")
                               .setDefaultValue("hello")
                               .setDescription("an optional option with a default value"))
            .addOption(new Option()
                               .setLongName("optional2")
                               .setDefaultValue("hello")
                               .setDescription("an optional option with a default value"))
            .addOption(new Option()
                               .setLongName("color")
                               .setDefaultValue("green")
                               .addChoice("blue").addChoice("red").addChoice("green")
                               .setDescription("a color"))
            .addArgument(new Argument()
                                 .setIndex(0)
                                 .setDescription("the first argument")
                                 .setArgName("arg1"))
            .addArgument(new Argument()
                                 .setIndex(1)
                                 .setDescription("the second argument")
                                 .setArgName("arg2"))
            .addArgument(new Argument()
                                 .setIndex(2)
                                 .setMultiValued(true)
                                 .setDescription("the third argument")
                                 .setArgName("arg3"));

    StringBuilder builder = new StringBuilder();
    cli.usage(builder);
    System.out.println(builder);

    List<String> arguments = new ArrayList<>();
    arguments.add("--mandatory");
    arguments.add("foo");
    arguments.add("-f");
    arguments.add("-m");
    arguments.add("haha");
    arguments.add("hoho");
    arguments.add("ohoh");
    arguments.add("--optional");
    arguments.add("oh no");
    arguments.add("--color");
    arguments.add("blue");
    arguments.add("foo.txt");
    arguments.add("bar.txt");
    arguments.add("haha.txt");
    arguments.add("hoho.txt");
    arguments.add("ohoh.txt");

    //Parsing Stage
    CommandLine commandLine = cli.parse(arguments);

//    Once parsed, you can retrieve the values of the options and arguments from the CommandLine
// object returned by the parse method:
    String opt = commandLine.getOptionValue("mandatory");
    System.out.println(opt);
    System.out.println(commandLine.isFlagEnabled("f"));
    System.out.println(commandLine.getOptionValues("multiple"));
    System.out.println(commandLine.getOptionValue("optional").toString());
    System.out.println(commandLine.getOptionValue("optional2").toString());
    System.out.println(commandLine.getOptionValue("color").toString());
    System.out.println(commandLine.getArgumentValue(0).toString());
    System.out.println(commandLine.getArgumentValue(1).toString());
    System.out.println(commandLine.getArgumentValues(2).toString());

  }
}
