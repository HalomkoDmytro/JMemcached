package net.devstudy.jmemchashed.server;


import net.devstudy.jmemchashed.server.impl.JMemcashedServerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class CLI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CLI.class);
    private static final List<String> QUIT_CMDS
            = Collections.unmodifiableList(Arrays.asList(new String[]{"q", "quit", "exit"}));

    public static void main(String[] args) {
        Thread.currentThread().setName("CLI-main thread");
        try {
            Server server = JMemcashedServerFactory.buildNewServer(null);
            server.start();
            waitForStopCommand(server);
        } catch (Exception e) {
            LOGGER.error(String.format("can't execute cmd: " + e.getMessage()), e);
        }
    }

    private static void waitForStopCommand(Server server) {
        try (Scanner scanner = new Scanner(System.in)) { //todo add charset
            while (true) {
                String cmd = scanner.nextLine();
                if (QUIT_CMDS.contains(cmd.toLowerCase())) {
                    server.stop();
                    break;
                } else {
                    LOGGER.error(String.format("Undefined command: %s! To shout down server pleas type: q"));
                }
            }

        }
    }

}
