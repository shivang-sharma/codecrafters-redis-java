package com.redis.eventloop;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventLoop implements IEventLoop{
    private BlockingQueue<AsyncConnectionHandler> concurrentClients;
    private final AtomicBoolean alive;
    public EventLoop(BlockingQueue<AsyncConnectionHandler> concurrentClients, AtomicBoolean alive) {
        this.concurrentClients = concurrentClients;
        this.alive = alive;
    }

    @Override
    public void start() {
        System.out.println("Event Loop Started ...");
        while (this.alive.get()) {
            try {
                AsyncConnectionHandler asyncTask = concurrentClients.take();
                asyncTask.execute();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void stop() {
        this.alive.set(false);
    }
}
