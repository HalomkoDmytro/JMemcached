package net.devstudy.jmemchashed.server;

import net.devstudy.jmemcashed.protocol.RequestConverter;
import net.devstudy.jmemcashed.protocol.ResponseConverter;

import java.net.Socket;
import java.util.concurrent.ThreadFactory;

public interface ServerConfig extends AutoCloseable {

    RequestConverter getRequestConverter();

    ResponseConverter getResponseConverter();

    ThreadFactory getWorkerThreadFactory();

    Storage getStorage();

    CommandHandler getCommandHandler();

    int getClearDataIntervalInMs();

    int getServerPort();

    int getInitThreadCount();

    int getMaxThreadCount();

    ClientSocketHandler buildNewClientSocketHandler(Socket clientSocket);
}
