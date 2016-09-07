package com.edgar.vertx.launcher;

import io.vertx.core.Launcher;

/**
 * Created by Edgar on 2016/4/21.
 *
 * @author Edgar  Date 2016/4/21
 */
public class RunMain {
    public static void main(String[] args) {
        new Launcher().execute("--help");
        new Launcher().execute("run", Server.class.getName());
//                           "--conf=target/config.json");
        System.out.println("starting");
    }
}
