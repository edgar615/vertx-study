package com.edgar.vertx.cli;

import io.vertx.core.cli.Argument;
import io.vertx.core.cli.CLI;
import io.vertx.core.cli.CommandLine;

import java.util.Arrays;

/**
 * Created by Edgar on 2016/9/6.
 *
 * @author Edgar  Date 2016/9/6
 */
public class ParsingMain {
    public static void main(String[] args) {
        CLI cli = CLI.create("some-name")
                .addArgument(new Argument()
                        .setIndex(0) //不设置index会按照声明的顺序设置index
                        .setDescription("the first argument")
                        .setArgName("arg1"))
                .addArgument(new Argument()
                        .setIndex(1)
                        .setDescription("the second argument")
                        .setArgName("arg2"));
        //    Usage generation
        StringBuilder builder = new StringBuilder();
        cli.usage(builder);
        System.out.println(builder);

        CommandLine commandLine = cli.parse(Arrays.asList(args));
        //检查参数
        System.out.println(commandLine.isValid());

        String opt = commandLine.getOptionValue("my-option");
        boolean flag = commandLine.isFlagEnabled("my-flag");
        String arg0 = commandLine.getArgumentValue(0);
    }
}
