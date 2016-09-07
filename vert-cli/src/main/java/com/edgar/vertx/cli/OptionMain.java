package com.edgar.vertx.cli;

import io.vertx.core.cli.CLI;
import io.vertx.core.cli.CommandLine;
import io.vertx.core.cli.Option;

import java.util.Arrays;

/**
 * Created by Edgar on 2016/9/6.
 *
 * @author Edgar  Date 2016/9/6
 */
public class OptionMain {
    public static void main(String[] args) {
        /**
         * option:  参数列表中使用key来区分的参数
         *
         * longname用--longname，shortname用-shortname
         */

        CLI cli = CLI.create("some-name")
                .setSummary("A command line interface illustrating the options valuation.")
                .addOption(new Option()
                        .setLongName("flag").setShortName("f").setFlag(true).setDescription("a flag"));
        cli.addOption(new Option()
                .setLongName("single").setShortName("s").setDescription("a single-valued option"));

        //可以接受多个参数
        cli.addOption(new Option()
                .setLongName("multiple").setShortName("m").setMultiValued(true)
                .setDescription("a multi-valued option"));

        //必填项
        cli.addOption(new Option()
                .setLongName("mandatory")
                .setRequired(true)
                .setDescription("a mandatory option"));

        //默认值
        cli.addOption(new Option()
                .setLongName("optional")
                .setDefaultValue("hello")
                .setDescription("an optional option with a default value"));

        //隐藏值
        cli.addOption(new Option()
                .setHidden(true)
                .setLongName("color")
                .setDefaultValue("green")
                .addChoice("blue").addChoice("red").addChoice("green")
                .setDescription("a color"));

        //帮助，在解析参数的时候可以用line.isAskingForHelp()来判断是否是帮助请求
        cli.addOption(
                new Option().setLongName("help").setShortName("h").setFlag(true).setHelp(true));

        //    Usage generation
        StringBuilder builder = new StringBuilder();
        cli.usage(builder);
        System.out.println(builder);

        CommandLine commandLine = cli.parse(Arrays.asList("--mandatory", "mandatory", "-m", "a", "b", "c", "-s", "a", "b", "c"));
        System.out.println(commandLine.isValid());
        System.out.println((String)commandLine.getOptionValue("f"));
        System.out.println(commandLine.isFlagEnabled("f"));
        System.out.println((String)commandLine.getOptionValue("mandatory"));
        System.out.println((String)commandLine.getOptionValue("h"));
        System.out.println((String)commandLine.getOptionValue("help"));
        System.out.println(commandLine.getOptionValues("multiple"));
        System.out.println(commandLine.getOptionValues("s"));
    }
}
