package net.devstudy.jmemchashed.client.impl;

import net.devstudy.jmemcashed.protocol.ObjectSerializer;
import net.devstudy.jmemcashed.protocol.RequestConverter;
import net.devstudy.jmemcashed.protocol.ResponseConverter;
import net.devstudy.jmemcashed.protocol.impl.DefaultObjectSerializer;
import net.devstudy.jmemcashed.protocol.impl.DefaultRequestConverter;
import net.devstudy.jmemcashed.protocol.impl.DefaultResponseConverter;
import net.devstudy.jmemchashed.client.ClientConfig;

class DefaultClientConfig implements ClientConfig {

    private final String host;
    private final int port;
    private final RequestConverter requestConverter;
    private final ResponseConverter responseConverter;
    private final ObjectSerializer objectSerializer;

    DefaultClientConfig(String host, int port) {
        this.host = host;
        this.port = port;
        this.requestConverter = new DefaultRequestConverter();
        this.responseConverter = new DefaultResponseConverter();
        this.objectSerializer = new DefaultObjectSerializer();
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public RequestConverter getRequestConverter() {
        return requestConverter;
    }

    @Override
    public ResponseConverter getResponseConverter() {
        return responseConverter;
    }

    @Override
    public ObjectSerializer getObjectSerializer() {
        return objectSerializer;
    }
}
