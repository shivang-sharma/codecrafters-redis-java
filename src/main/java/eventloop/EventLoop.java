package eventloop;

import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventLoop implements IEventLoop{
    private BlockingQueue<AsyncTask> concurrentClients;
    private final AtomicBoolean alive;
    public EventLoop(BlockingQueue<AsyncTask> concurrentClients, AtomicBoolean alive) {
        this.concurrentClients = concurrentClients;
        this.alive = alive;
    }

    @Override
    public void start() {
        while (this.alive.get()) {
            try {
                AsyncTask asyncTask = concurrentClients.take();
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
