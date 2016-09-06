package com.edgar.vertx.cli;

import io.vertx.core.cli.CLI;
import io.vertx.core.cli.Option;
import io.vertx.core.cli.TypedArgument;
import io.vertx.core.cli.TypedOption;

import java.io.File;

/**
 * Created by Edgar on 2016/9/6.
 *
 * @author Edgar  Date 2016/9/6
 */
public class TypedMain {
    public static void main(String[] args) {
        /**
         * option:  参数列表中使用key来区分的参数
         *
         * longname用--longname，shortname用-shortname
         */

        CLI cli = CLI.create("copy")
                .setSummary("A command line interface to copy files.")
                .addOption(new TypedOption<Boolean>()
                        .setType(Boolean.class)
                        .setLongName("directory")
                        .setShortName("R")
                        .setDescription("enables directory support")
                        .setFlag(true))
                .addArgument(new TypedArgument<File>()
                        .setType(File.class)
                        .setIndex(0)
                        .setDescription("The source")
                        .setArgName("source"))
                .addArgument(new TypedArgument<File>()
                        .setType(File.class)
                        .setIndex(0)
                        .setDescription("The destination")
                        .setArgName("target"));

        //    Usage generation
        StringBuilder builder = new StringBuilder();
        cli.usage(builder);
        System.out.println(builder);

//        CommandLine commandLine = cli.parse(userCommandLineArguments);
//        boolean flag = commandLine.getOptionValue("R");
//        File source = commandLine.getArgumentValue("source");
//        File target = commandLine.getArgumentValue("target");
    }
}
