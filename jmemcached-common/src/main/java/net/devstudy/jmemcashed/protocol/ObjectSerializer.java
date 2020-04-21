package net.devstudy.jmemcashed.protocol;

public interface ObjectSerializer {

    byte[] toByteArray(Object o);

    Object fromByteArray(byte[] array);
}
