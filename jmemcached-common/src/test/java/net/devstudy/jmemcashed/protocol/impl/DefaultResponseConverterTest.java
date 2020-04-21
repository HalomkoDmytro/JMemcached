package net.devstudy.jmemcashed.protocol.impl;

import net.devstudy.jmemcashed.protocol.model.Response;
import net.devstudy.jmemcashed.protocol.model.Status;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DefaultResponseConverterTest {

    private final DefaultResponseConverter defaultResponseConverter = new DefaultResponseConverter();

    @Test
    public void readResponseWithoutData() throws IOException {
        Response response = defaultResponseConverter
                .readResponse(new ByteArrayInputStream(new byte[]{16, 0, 0})); //version/status/flag
        assertEquals(Status.ADDED, response.getStatus());
        assertFalse(response.hasData());
    }

    @Test
    public void readResponseWithData() throws IOException {
        Response response = defaultResponseConverter
                .readResponse(new ByteArrayInputStream(new byte[]{16,0,1, 0,0,0,3, 1,2,3}));//version/status/flag/int length data(4)/byte array(3)
        assertEquals(Status.ADDED, response.getStatus());
        assertTrue(response.hasData());
        assertArrayEquals(new byte[]{1,2,3}, response.getData());
    }

    @Test
    public void writeResponseWithoutData() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Response response = new Response(Status.GOTTEN);
        defaultResponseConverter.writeResponse(out, response);
        assertArrayEquals(new byte[]{16,2,0}, out.toByteArray());
    }

    @Test
    public void writeResponseWithData() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Response response = new Response(Status.ADDED, new byte[] {1, 2, 3});
        defaultResponseConverter.writeResponse(out, response);
        //version/status/flag/int length data(4)/byte array(3)
        assertArrayEquals(new byte[]{16,0,1, 0,0,0,3, 1,2,3}, out.toByteArray());
    }
}
