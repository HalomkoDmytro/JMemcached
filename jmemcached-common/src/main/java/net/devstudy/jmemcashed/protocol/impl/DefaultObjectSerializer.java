package net.devstudy.jmemcashed.protocol.impl;

import net.devstudy.jmemcashed.exception.JMemcashedException;
import net.devstudy.jmemcashed.protocol.ObjectSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class DefaultObjectSerializer implements ObjectSerializer {

    @Override
    public byte[] toByteArray(Object o) {
        if (o == null) {
            return null;
        }
        if (!(o instanceof Serializable)) {
            throw new JMemcashedException(String.format("Class %s should implement java.io.Serializable interface",
                    o.getClass().getName()));
        }

        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream);
            out.writeObject(o);
            out.flush();
        } catch (IOException e) {
            throw new JMemcashedException("Can't convert object to byte array: " + e.getMessage(), e);
        }
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public Object fromByteArray(byte[] array) {
        if (array == null) {
            return null;
        }
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new ByteArrayInputStream(array));
            return in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new JMemcashedException("Can't convert array to byte object: " + e.getMessage(), e);
        }
    }
}
