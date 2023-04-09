import eventloop.AsyncTask;
import eventloop.EventLoop;
import eventloop.MultithreadedEventLoop;
import eventloop.Task;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.net.ServerSocket;
import java.net.Socket;


public class Main {
  public static void main(String[] args) {
    int eventLoopThreads = 1;
    int workerThreads = 5;
    BlockingQueue<AsyncTask> concurrentTasks = new ArrayBlockingQueue<>(5);
    EventLoop eventLoop = new EventLoop(concurrentTasks, new AtomicBoolean(true));
    ExecutorService executorService = Executors.newFixedThreadPool(eventLoopThreads + workerThreads);
    MultithreadedEventLoop multithreadedEventLoop = new MultithreadedEventLoop(executorService, eventLoop);
    multithreadedEventLoop.start();
    System.out.println("Started");
    ServerSocket serverSocket = null;
    Socket clientSocket = null;
    int port = 6379;
    try {
      serverSocket = new ServerSocket(port);
      serverSocket.setReuseAddress(true);
      // Wait for connection from client.
      while (true) {
        clientSocket = serverSocket.accept();
        concurrentTasks.put(new AsyncTask(executorService, new Task(clientSocket)));
      }
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
