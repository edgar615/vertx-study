package com.edgar.vertx.cli;

import io.vertx.core.cli.CLI;

/**
 * Created by Edgar on 2016/9/6.
 *
 * @author Edgar  Date 2016/9/6
 */
public class AnnotatedCliMain {
    public static void main(String[] args) {
        CLI cli = CLI.create(AnnotatedCli.class);
        StringBuilder builder = new StringBuilder();
        cli.usage(builder);
        System.out.println(builder);

//        CommandLine commandLine = cli.parse(userCommandLineArguments);
//        AnnotatedCli instance = new AnnotatedCli();
//        CLIConfigurator.inject(commandLine, instance);
    }
}
