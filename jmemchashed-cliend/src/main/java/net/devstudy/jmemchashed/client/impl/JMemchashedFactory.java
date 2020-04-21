package net.devstudy.jmemchashed.client.impl;

import net.devstudy.jmemchashed.client.Client;

import java.io.IOException;

public class JMemchashedFactory {

    public static Client buildNewClient(String host, int port) throws IOException {
        return new DefaultClient(new DefaultClientConfig(host, port));
    }

    public static Client buildNewClient(String host) throws IOException {
        return buildNewClient(host, 9100);
    }

    public static Client buildNewClient() throws IOException {
        return buildNewClient("localhost");
    }
}
