package com.edgar.vertx.cli;

import io.vertx.core.cli.Argument;
import io.vertx.core.cli.CLI;
import io.vertx.core.cli.CommandLine;
import io.vertx.core.cli.Option;

import java.util.Arrays;

/**
 * Created by Edgar on 2016/9/6.
 *
 * @author Edgar  Date 2016/9/6
 */
public class ArgMain {
    public static void main(String[] args) {
        /**
         * arguments :  参数列表中使用索引来区分的参数
         *
         */

        CLI cli = CLI.create("some-name")
                .addArgument(new Argument()
                        .setIndex(0) //不设置index会按照声明的顺序设置index
                        .setDescription("the first argument")
                        .setArgName("arg1"))
                .addArgument(new Argument()
                        .setIndex(1)
                        .setDescription("the second argument")
                        .setArgName("arg2"));

        //隐藏值
        cli.addArgument(new Argument()
                .setIndex(2)
                .setArgName("hidden")
                .setHidden(true)
                .setDescription("a hidden argument"));

        //默认值
        cli.addArgument(new Argument()
                .setIndex(3)
                .setArgName("default")
                .setDefaultValue("default")
                .setDescription("a default argument"));

        //必填
        cli.addArgument(new Argument()
                .setIndex(4)
                .setArgName("required")
                .setRequired(false)//默认都是必填
                .setDescription("a required argument"));

        //可以接受多个参数，只能最后一个参数使用
        cli.addArgument(new Argument()
                .setIndex(5)
                .setDescription("a multi-valued argument")
                .setMultiValued(true));


        //    Usage generation
        StringBuilder builder = new StringBuilder();
        cli.usage(builder);
        System.out.println(builder);


        CommandLine commandLine = cli.parse(Arrays.asList("a", "b", "c", "d", "e", "f", "g"));
        System.out.println(commandLine.isValid());
        System.out.println((String)commandLine.getArgumentValue(0));
        System.out.println((String)commandLine.getArgumentValue(1));
        System.out.println((String)commandLine.getArgumentValue(2));
        System.out.println((String)commandLine.getArgumentValue(3));
        System.out.println((String)commandLine.getArgumentValue(4));
        System.out.println(commandLine.getArgumentValues(5));
    }
}
