package com.redis.eventloop;

import java.util.concurrent.ExecutorService;

public class MultithreadedEventLoop implements IEventLoop {
    private final ExecutorService executorService;
    private final IEventLoop eventLoop;
    public MultithreadedEventLoop(ExecutorService executorService, IEventLoop eventLoop) {
        this.executorService = executorService;
        this.eventLoop = eventLoop;
    }
    @Override
    public void start() {
        System.out.println("Starting Event Loop ...");
        executorService.execute(eventLoop::start);
    }
    @Override
    public void stop() {
        eventLoop.stop();
        executorService.shutdown();
    }
}
