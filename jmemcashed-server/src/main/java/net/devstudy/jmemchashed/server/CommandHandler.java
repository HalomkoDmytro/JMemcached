package net.devstudy.jmemchashed.server;

import net.devstudy.jmemcashed.protocol.model.Request;
import net.devstudy.jmemcashed.protocol.model.Response;

public interface CommandHandler {

    Response handle(Request request);
}
