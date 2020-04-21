package net.devstudy.jmemchashed.client.impl;


import net.devstudy.jmemcashed.protocol.ObjectSerializer;
import net.devstudy.jmemcashed.protocol.RequestConverter;
import net.devstudy.jmemcashed.protocol.ResponseConverter;
import net.devstudy.jmemcashed.protocol.model.Command;
import net.devstudy.jmemcashed.protocol.model.Request;
import net.devstudy.jmemcashed.protocol.model.Response;
import net.devstudy.jmemcashed.protocol.model.Status;
import net.devstudy.jmemchashed.client.ClientConfig;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DefaultClientTest {

    private DefaultClient defaultClient;
    private Socket socket;
    private OutputStream out;
    private InputStream in;

    private ClientConfig clientConfig;
    private RequestConverter requestConverter;
    private ResponseConverter responseConverter;
    private ObjectSerializer objectSerializer;

    @Before
    public void before() throws IOException {
        socket = mock(Socket.class);
        out = mock(OutputStream.class);
        in = mock(InputStream.class);
        when(socket.getInputStream()).thenReturn(in);
        when(socket.getOutputStream()).thenReturn(out);

        clientConfig = mock(ClientConfig.class);
        requestConverter = mock(RequestConverter.class);
        responseConverter = mock(ResponseConverter.class);
        objectSerializer = mock(ObjectSerializer.class);
        when(clientConfig.getRequestConverter()).thenReturn(requestConverter);
        when(clientConfig.getResponseConverter()).thenReturn(responseConverter);
        when(clientConfig.getObjectSerializer()).thenReturn(objectSerializer);

        defaultClient = new DefaultClient(clientConfig) {
            @Override
            protected Socket createSocket(ClientConfig clientConfig) throws IOException {
                return socket;
            }
        };
    }

    @Test
    public void makeRequest() throws IOException {
        Request request = new Request(Command.CLEAR);
        when(responseConverter.readResponse(in)).thenReturn(new Response(Status.CLEARED));

        Response response = defaultClient.makeRequest(request);
        assertEquals(Status.CLEARED, response.getStatus());
        verify(requestConverter).writeRequest(out, request);
        verify(responseConverter).readResponse(in);
    }

    @Test
    public void putSimple() throws IOException {
        String key = "key";
        Object value = "value";
        byte[] array = {1, 2, 3};

        when(responseConverter.readResponse(in)).thenReturn(new Response(Status.ADDED));
        when(objectSerializer.toByteArray(value)).thenReturn(array);

        Status status = defaultClient.put(key, value);
        assertEquals(Status.ADDED, status);
        verify(objectSerializer).toByteArray(value);
        verify(requestConverter).writeRequest(same(out), equalTo(new Request(Command.PUT, key, null, array)));
    }

    @Test
    public void putFull() throws IOException {
        String key = "key";
        Object value = "value";
        byte[] array = {1, 2, 3};

        when(responseConverter.readResponse(in)).thenReturn(new Response(Status.REPLACED));
        when(objectSerializer.toByteArray(value)).thenReturn(array);
        Status status = defaultClient.put(key, value, 1, TimeUnit.MILLISECONDS);

        assertEquals(Status.REPLACED, status);
        verify(objectSerializer).toByteArray(value);
        verify(requestConverter).writeRequest(same(out), equalTo(new Request(Command.PUT, key, 1L, array)));
    }

    @Test
    public void putFullWithTTL() throws IOException {
        String key = "key";
        Object value = "value";
        byte[] array = {1, 2, 3};

        when(responseConverter.readResponse(in)).thenReturn(new Response(Status.REPLACED));
        when(objectSerializer.toByteArray(value)).thenReturn(array);
        Status status = defaultClient.put(key, value, 1, null);

        assertEquals(Status.REPLACED, status);
        verify(objectSerializer).toByteArray(value);
        verify(requestConverter).writeRequest(same(out), equalTo(new Request(Command.PUT, key, null, array)));
    }

    @Test
    public void get() throws IOException {
        String key = "key";
        Object value = "value";
        byte[] array = {1, 2, 3};
        when(responseConverter.readResponse(in)).thenReturn(new Response(Status.GOTTEN, array));
        when(objectSerializer.fromByteArray(array)).thenReturn(value);

        final String result = defaultClient.get(key);
        assertEquals(value, result);
        verify(objectSerializer).fromByteArray(array);
        verify(requestConverter).writeRequest(same(out), equalTo(new Request(Command.GET, key)));
    }

    @Test
    public void remove() throws IOException {
        String key = "key";
        when(responseConverter.readResponse(in)).thenReturn(new Response(Status.REMOVED));

        final Status status = defaultClient.remove(key);
        assertEquals(Status.REMOVED, status);
        verify(requestConverter).writeRequest(same(out), equalTo(new Request(Command.REMOVE, key)));
    }

    @Test
    public void clear() throws IOException {
        when(responseConverter.readResponse(in)).thenReturn(new Response(Status.CLEARED));

        final Status status = defaultClient.clear();
        assertEquals(Status.CLEARED, status);
        verify(requestConverter).writeRequest(same(out), equalTo(new Request(Command.CLEAR)));
    }

    @Test
    public void closed() throws IOException {
        defaultClient.close();

        verify(socket).close();
    }

    private Request equalTo(final Request request) {
        return argThat(new ArgumentMatcher<Request>() {
            @Override
            public boolean matches(Request argument) {
                return Objects.equals(request.getCommand(), argument.getCommand()) &&
                        Objects.equals(request.getTtl(), argument.getTtl()) &&
                        Objects.equals(request.getKey(), argument.getKey()) &&
                        Objects.equals(request.getData(), argument.getData());
            }
        });
    }
}
