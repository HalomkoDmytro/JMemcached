package net.devstudy.jmemchashed.client.impl;

import net.devstudy.jmemcashed.protocol.ObjectSerializer;
import net.devstudy.jmemcashed.protocol.RequestConverter;
import net.devstudy.jmemcashed.protocol.ResponseConverter;
import net.devstudy.jmemcashed.protocol.model.Command;
import net.devstudy.jmemcashed.protocol.model.Request;
import net.devstudy.jmemcashed.protocol.model.Response;
import net.devstudy.jmemcashed.protocol.model.Status;
import net.devstudy.jmemchashed.client.Client;
import net.devstudy.jmemchashed.client.ClientConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

class DefaultClient implements Client {

    private final RequestConverter requestConverter;
    private final ResponseConverter responseConverter;
    private final ObjectSerializer objectSerializer;

    private final Socket socket;
    private final InputStream inputStream;
    private final OutputStream outputStream;

    DefaultClient(ClientConfig clientConfig) throws IOException {
        this.objectSerializer = clientConfig.getObjectSerializer();
        this.requestConverter = clientConfig.getRequestConverter();
        this.responseConverter = clientConfig.getResponseConverter();

        this.socket = createSocket(clientConfig);
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
    }

    protected Socket createSocket(ClientConfig clientConfig) throws IOException {
        final Socket socket = new Socket(clientConfig.getHost(), clientConfig.getPort());
        socket.setKeepAlive(true);
        return socket;
    }

    protected Response makeRequest(Request request) throws IOException {
        requestConverter.writeRequest(outputStream, request);
        return responseConverter.readResponse(inputStream);
    }

    @Override
    public Status put(String key, Object o) throws IOException {
        return put(key, o, null, null);
    }

    @Override
    public Status put(String key, Object o, Integer ttl, TimeUnit timeUnit) throws IOException {
        byte[] data = objectSerializer.toByteArray(o);
        Long requestTTL = ttl != null && timeUnit != null ? timeUnit.toMillis(ttl) : null;
        Response response = makeRequest(new Request(Command.PUT, key, requestTTL, data));
        return response.getStatus();
    }

    @Override
    public <T> T get(String key) throws IOException {
        Response response = makeRequest(new Request(Command.GET, key));
        return (T) objectSerializer.fromByteArray(response.getData());
    }

    @Override
    public Status remove(String key) throws IOException {
        Response response = makeRequest(new Request(Command.REMOVE, key));
        return response.getStatus();
    }

    @Override
    public Status clear() throws IOException {
        Response response = makeRequest(new Request(Command.CLEAR));
        return response.getStatus();
    }

    @Override
    public void close() throws IOException {
        this.socket.close();
    }
}
