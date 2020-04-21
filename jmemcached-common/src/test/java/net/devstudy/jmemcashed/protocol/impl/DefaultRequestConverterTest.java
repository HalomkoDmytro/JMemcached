package net.devstudy.jmemcashed.protocol.impl;

import net.devstudy.jmemcashed.exception.JMemcashedException;
import net.devstudy.jmemcashed.protocol.model.Command;
import net.devstudy.jmemcashed.protocol.model.Request;
import org.apache.commons.lang.StringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class DefaultRequestConverterTest {

    private final DefaultRequestConverter defaultRequestConverter = new DefaultRequestConverter();

    private byte[] requestClear = {16, 0, 0};
    private byte[] requestPut = {16,
            1, 7, 3,
            49, 50, 51,
            0, 0, 0, 0, 0, 0, 0, 5,
            0, 0, 0, 3,
            1, 2, 3};

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getFlagsByteEmpty() {
        Request request = new Request(Command.CLEAR);
        byte flags = defaultRequestConverter.getFlagsByte(request);
        assertEquals(0, flags);
    }

    @Test
    public void getFlagsByteAll() {
        Request request = new Request(Command.CLEAR, "key", System.currentTimeMillis(), new byte[]{1});
        byte flags = defaultRequestConverter.getFlagsByte(request);
        assertEquals(7, flags);
    }

    @Test
    public void writeKeySuccess() throws IOException {
        DataOutputStream dataOutputStream = spy(new DataOutputStream(mock(OutputStream.class)));
        String key = "key";
        defaultRequestConverter.writeKey(dataOutputStream, new Request(Command.GET, key));

        verify(dataOutputStream).write(key.getBytes(StandardCharsets.US_ASCII));
        verify(dataOutputStream).writeByte(3);
    }

    @Test
    public void writeKeyFailed() throws IOException {
        String key = StringUtils.repeat("a", 128);
        thrown.expect(JMemcashedException.class);
        thrown.expectMessage(is("Key length should be  <= 127 bytes for key=" + key));

        DataOutputStream dataOutputStream = new DataOutputStream(null);
        defaultRequestConverter.writeKey(dataOutputStream, new Request(Command.CLEAR, key));
    }

    @Test
    public void readSimpleRequest() throws IOException {
        Request request = defaultRequestConverter.readRequest(new ByteArrayInputStream(requestClear)); // version/command/flag
        assertEquals(Command.CLEAR, request.getCommand());
        assertFalse(request.hasKey());
        assertFalse(request.hasTtl());
        assertFalse(request.hasData());
    }

    @Test
    public void readCommandRequest() throws IOException {
        Request request = defaultRequestConverter
                .readRequest(new ByteArrayInputStream(requestPut)); // version/command/flag
        assertEquals(Command.PUT, request.getCommand());
        assertTrue(request.hasKey());
        assertEquals("123", request.getKey());
        assertTrue(request.hasTtl());
        assertEquals(Long.valueOf(5L), request.getTtl());
        assertTrue(request.hasData());
        assertArrayEquals(new byte[]{1, 2, 3}, request.getData());
    }

    @Test
    public void writeRequestWithoutData() throws IOException{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        defaultRequestConverter.writeRequest(out, new Request(Command.CLEAR));

        assertArrayEquals(requestClear, out.toByteArray());
    }

    @Test
    public void writeRequestWithData() throws IOException{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        defaultRequestConverter.writeRequest(out, new Request(Command.PUT, "123", 5L, new byte[]{1,2,3}));

        assertArrayEquals(requestPut, out.toByteArray());
    }
}
