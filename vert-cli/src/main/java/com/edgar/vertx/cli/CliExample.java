package com.edgar.vertx.cli;

import io.vertx.core.cli.Argument;
import io.vertx.core.cli.CLI;
import io.vertx.core.cli.CommandLine;
import io.vertx.core.cli.Option;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Edgar on 2016/3/16.
 *
 * @author Edgar  Date 2016/3/16
 */
public class CliExample {


//  Vert.x CLI provides a model to describe your command line interface, but also a parser. This
// parser supports different types of syntax:
//
//  POSIX like options (ie. tar -zxvf foo.tar.gz)
//
//  GNU like long options (ie. du --human-readable --max-depth=1)
//
//  Java like properties (ie. java -Djava.awt.headless=true -Djava.net.useSystemProxies=true Foo)
//
//  Short options with value attached (ie. gcc -O2 foo.c)
//
//  Long options with single hyphen (ie. ant -projecthelp)
//
//  Using the CLI api is a 3-steps process:
//
//  The definition of the command line interface
//
//          The parsing of the user command line
//
//                  The query / interrogation
//

  public static void main(String[] args) {
    CLI cli = CLI.create("copy")
            .setSummary("A command line interface to copy files.")
            .addOption(new Option()
                               .setLongName("directory")
                               .setShortName("R")
                               .setDescription("enables directory support")
                               .setFlag(true))
            .addArgument(new Argument()
                                 .setIndex(0)
                                 .setDescription("The source")
                                 .setArgName("source"))
            .addArgument(new Argument()
                                 .setIndex(0)
                                 .setDescription("The destination")
                                 .setArgName("target"));

//    Options
//
//    An Option is a command line parameter identified by a key present in the user command line.
// Options must have at least a long name or a short name. Long name are generally used using a
// -- prefix, while short names are used with a single -. Options can get a description displayed
// in the usage (see below). Options can receive 0, 1 or several values. An option receiving 0
// values is a flag, and must be declared using setFlag. By default, options receive a single
// value, however, you can configure the option to receive several values using setMultiValued:

    cli = CLI.create("some-name")
            .setSummary("A command line interface illustrating the options valuation.")
            .addOption(new Option()
                               .setLongName("flag").setShortName("f").setFlag(true)
                               .setDescription("a flag"))
            .addOption(new Option()
                               .setLongName("single").setShortName("s")
                               .setDescription("a single-valued option"))
            .addOption(new Option()
                               .setLongName("multiple").setShortName("m").setMultiValued(true)
                               .setDescription("a multi-valued option"));

    //Options can be marked as mandatory. A mandatory option not set in the user command line
    // throws an exception during the parsing:
    cli = CLI.create("some-name")
            .addOption(new Option()
                               .setLongName("mandatory")
                               .setRequired(true)
                               .setDescription("a mandatory option"));


//    Non-mandatory options can have a default value. This value would be used if the user does
// not set the option in the command line:

    cli = CLI.create("some-name")
            .addOption(new Option()
                               .setLongName("optional")
                               .setDefaultValue("hello")
                               .setDescription("an optional option with a default value"));

//    An option can be hidden using the setHidden method. Hidden option are not listed in the
// usage, but can still be used in the user command line (for power-users).
//
//    If the option value is contrained to a fixed set, you can set the different acceptable
// choices:
    cli = CLI.create("some-name")
            .addOption(new Option()
                               .setLongName("color")
                               .setDefaultValue("green")
                               .addChoice("blue").addChoice("red").addChoice("green")
                               .setDescription("a color"));

//    Arguments
//
//    Unlike options, arguments do not have a key and are identified by their index. For example, in java com.acme.Foo, com.acme.Foo is an argument.
    //Arguments do not have a name, there are identified using a 0-based index. The first parameter has the index 0
    cli = CLI.create("some-name")
            .addArgument(new Argument()
                                 .setIndex(0)
                                 .setDescription("the first argument")
                                 .setArgName("arg1"))
            .addArgument(new Argument()
                                 .setIndex(1)
                                 .setDescription("the second argument")
                                 .setArgName("arg2"));



//    If you don’t set the argument indexes, it computes it automatically by using the declaration order.

    cli = CLI.create("some-name")
            // will have the index 0
            .addArgument(new Argument()
                                 .setDescription("the first argument")
                                 .setArgName("arg1"))
                    // will have the index 1
            .addArgument(new Argument()
                                 .setDescription("the second argument")
                                 .setArgName("arg2"));

//    The argName is optional and used in the usage message.
//
//    As options, Argument can:
//
//    be hidden using setHidden
//
//    be mandatory using setRequired
//
//    have a default value using setDefaultValue
//
//    receive several values using setMultiValued - only the last argument can be multi-valued.
//
    cli = CLI.create("copy")
            .setSummary("A command line interface to copy files.")
            .addOption(new Option()
                               .setLongName("directory")
                               .setShortName("R")
                               .setDescription("enables directory support")
                               .setFlag(true))
            .addArgument(new Argument()
                                 .setIndex(0)
                                 .setDescription("The source")
                                 .setArgName("source"))
            .addArgument(new Argument()
                                 .setIndex(1)
                                 .setDescription("The destination")
                                 .setArgName("target"));

//    Usage generation
    StringBuilder builder = new StringBuilder();
    cli.usage(builder);
    System.out.println(builder);

    //Parsing Stage
    CommandLine commandLine = cli.parse(Arrays.asList(args));

//    Query / Interrogation Stage
//
//    Once parsed, you can retrieve the values of the options and arguments from the CommandLine object returned by the parse method:
    String opt = commandLine.getOptionValue("my-option");
    boolean flag = commandLine.isFlagEnabled("my-flag");
    String arg0 = commandLine.getArgumentValue(0);

//    One of your option can have been marked as "help". If a user command line enabled a "help" option, the validation won’t failed, but give you the opportunity to check if the user asks for help:
//    cli = CLI.create("test")
//            .addOption(
//                    new Option().setLongName("help").setShortName("h").setFlag(true).setHelp(true))
//            .addOption(
//                    new Option().setLongName("mandatory").setRequired(true));
//
//    CommandLine line = cli.parse(Collections.singletonList("-h"));
//
//// The parsing does not fail and let you do:
//    if (!line.isValid() && line.isAskingForHelp()) {
//      StringBuilder builder = new StringBuilder();
//      cli.usage(builder);
//      stream.print(builder.toString());
//    }

//    Typed options and arguments
//
//    The described Option and Argument classes are untyped, meaning that the only get String values.
//
//    TypedOption and TypedArgument let you specify a type, so the (String) raw value is converted to the specified type.
//
//    Instead of Option and Argument, use TypedOption and TypedArgument in the CLI definition:

//    CLI cli = CLI.create("copy")
//            .setSummary("A command line interface to copy files.")
//            .addOption(new TypedOption<Boolean>()
//                               .setType(Boolean.class)
//                               .setLongName("directory")
//                               .setShortName("R")
//                               .setDescription("enables directory support")
//                               .setFlag(true))
//            .addArgument(new TypedArgument<File>()
//                                 .setType(File.class)
//                                 .setIndex(0)
//                                 .setDescription("The source")
//                                 .setArgName("source"))
//            .addArgument(new TypedArgument<File>()
//                                 .setType(File.class)
//                                 .setIndex(0)
//                                 .setDescription("The destination")
//                                 .setArgName("target"));



//    The vert.x CLI is able to convert to classes:
//
//    having a constructor with a single String argument, such as File or JsonObject
//
//    with a static from or fromString method
//
//    with a static valueOf method, such as primitive types and enumeration
//
//    In addition, you can implement your own Converter and instruct the CLI to use this converter:

//    cli = CLI.create("some-name")
//            .addOption(new TypedOption<Person>()
//                               .setType(Person.class)
//                               .setConverter(new PersonConverter())
//                               .setLongName("person"));

//    Using annotations
//    cli = CLI.create(AnnotatedCli.class);
//    CommandLine commandLine = cli.parse(userCommandLineArguments);
//    AnnotatedCli instance = new AnnotatedCli();
//    CLIConfigurator.inject(commandLine, instance);

  }
}
