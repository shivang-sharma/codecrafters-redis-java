package com.redis.eventloop;

import java.util.concurrent.ExecutorService;

public class AsyncConnectionHandler implements IConnectionHandler {
    private final ExecutorService executorService;
    private final IConnectionHandler connectionHandler;

    public AsyncConnectionHandler(ExecutorService executorService, IConnectionHandler connectionHandler) {
        this.executorService = executorService;
        this.connectionHandler = connectionHandler;
    }

    @Override
    public void execute() {
        executorService.execute(connectionHandler::execute);
    }
}
