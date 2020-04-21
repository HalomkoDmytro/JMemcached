package net.devstudy.jmemchashed.server.impl;

import net.devstudy.jmemcashed.exception.JMemcashedException;
import net.devstudy.jmemcashed.protocol.model.Command;
import net.devstudy.jmemcashed.protocol.model.Request;
import net.devstudy.jmemcashed.protocol.model.Response;
import net.devstudy.jmemcashed.protocol.model.Status;
import net.devstudy.jmemchashed.server.CommandHandler;
import net.devstudy.jmemchashed.server.ServerConfig;
import net.devstudy.jmemchashed.server.Storage;

class DefaultCommandHandler implements CommandHandler {

    private final Storage storage;

    DefaultCommandHandler(ServerConfig serverConfig) {
        this.storage = serverConfig.getStorage();
    }

    @Override
    public Response handle(Request request) {
        Status status;
        byte[] data = null;
        if (request.getCommand() == Command.CLEAR) {
            status = storage.clear();
        } else if (request.getCommand() == Command.PUT) {
            status = storage.put(request.getKey(), request.getTtl(), request.getData());
        } else if (request.getCommand() == Command.REMOVE) {
            status = storage.remove(request.getKey());
        } else if (request.getCommand() == Command.GET) {
            data = storage.get(request.getKey());
            status = data == null ? Status.NOT_FOUND : Status.GOTTEN;
        } else {
            throw new JMemcashedException("Unsupported command: " + request.getCommand());
        }
        return new Response(status, data);
    }
}
