package net.devstudy.jmemchashed.server;

import net.devstudy.jmemcashed.protocol.model.Status;

public interface Storage extends AutoCloseable {

    Status put(String key, Long ttl, byte[] data);

    byte[] get(String key);

    Status remove(String key);

    Status clear();
}
