package net.devstudy.jmemcashed.protocol.impl;

import net.devstudy.jmemcashed.exception.JMemcashedException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import test.SerializableFailedClass;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DefaultObjectSerializerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final DefaultObjectSerializer defaultObjectSerializer = new DefaultObjectSerializer();

    private final Object testObject = "Test";
    private final byte[] testObjectArray = {-84, -19, 0, 5, 116, 0, 4, 84, 101, 115, 116};
    private final byte[] testIOExceptionDuringDeserialization = {-84, -19, 0, 5, 115, 114, 0, 28, 116, 101, 115, 116,
            46, 83, 101, 114, 105, 97, 108, 105, 122, 97, 98, 108, 101, 70, 97, 105, 108, 101, 100, 67, 108, 97, 115,
            115, -88, 79, -1, -107, 1, -38, 92, -71, 2, 0, 0, 120, 112};
    private final byte[] testNotFoundException = {-84, -19, 0, 5, 115, 114, 0, 3, 97, 46, 66, 56, 54, 57, -101, -3,
            120, 66, 4, 2, 0, 0, 120, 112};

    @Test
    public void toByteArraySuccess() {
        byte[] actual = defaultObjectSerializer.toByteArray(testObject);
        assertArrayEquals(testObjectArray, actual);
    }

    @Test
    public void toByteArrayNull() {
        assertNull(defaultObjectSerializer.toByteArray(null));
    }

    @Test
    public void toByteArraySerializableException() {
        thrown.expect(JMemcashedException.class);
        thrown.expectMessage(is("Class java.lang.Object should implement java.io.Serializable interface"));
        defaultObjectSerializer.toByteArray(new Object());
    }

    @Test
    public void toByteArrayWriteException() {
        thrown.expect(JMemcashedException.class);
        thrown.expectMessage(is("Can't convert object to byte array: WRITE IO"));
        thrown.expectCause(isA(IOException.class));
        defaultObjectSerializer.toByteArray(new SerializableFailedClass());

    }

    @Test
    public void fromByteArraySuccess() {
        String actual = (String) defaultObjectSerializer.fromByteArray(testObjectArray);
        assertEquals(testObject, actual);
    }

    @Test
    public void fromByteArrayNull() {
        assertNull(defaultObjectSerializer.fromByteArray(null));
    }

    @Test
    public void fromByteArrayReadException() {
        thrown.expect(JMemcashedException.class);
        thrown.expectMessage(is("Can't convert array to byte object: READ IO"));
        thrown.expectCause(isA(IOException.class));
        defaultObjectSerializer.fromByteArray(testIOExceptionDuringDeserialization);
    }

    @Test
    public void fromByteArrayReadNotFoundException() {
        thrown.expect(JMemcashedException.class);
        thrown.expectMessage(is("Can't convert array to byte object: a.B"));
        thrown.expectCause(isA(ClassNotFoundException.class));
        defaultObjectSerializer.fromByteArray(testNotFoundException);

    }
}
