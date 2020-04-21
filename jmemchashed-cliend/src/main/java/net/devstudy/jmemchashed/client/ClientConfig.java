package net.devstudy.jmemchashed.client;

import net.devstudy.jmemcashed.protocol.ObjectSerializer;
import net.devstudy.jmemcashed.protocol.RequestConverter;
import net.devstudy.jmemcashed.protocol.ResponseConverter;

public interface ClientConfig {

    String getHost();

    int getPort();

    RequestConverter getRequestConverter();

    ResponseConverter getResponseConverter();

    ObjectSerializer getObjectSerializer();
}
