package net.devstudy.jmemchashed.server.impl;

import net.devstudy.jmemchashed.server.Server;

import java.util.Properties;

public class JMemcashedServerFactory {

    public static Server buildNewServer(Properties overrideApplicationProperties) {
        return new DefaultServer(new DefaultServerConfig(overrideApplicationProperties));
    }
}
