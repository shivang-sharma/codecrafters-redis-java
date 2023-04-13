import com.redis.eventloop.*;
import com.redis.protocol.IRespProtocol;
import com.redis.protocol.command.ICommand;

import java.io.IOException;
import java.util.ArrayList;
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
    BlockingQueue<AsyncConnectionHandler> concurrentTasks = new ArrayBlockingQueue<>(5);
    EventLoop eventLoop = new EventLoop(concurrentTasks, new AtomicBoolean(true));
    ExecutorService executorService = Executors.newFixedThreadPool(eventLoopThreads + workerThreads);
    MultithreadedEventLoop multithreadedEventLoop = new MultithreadedEventLoop(executorService, eventLoop);
    multithreadedEventLoop.start();
    ServerSocket serverSocket = null;
    Socket clientSocket = null;
    int port = 6379;
    try {
      serverSocket = new ServerSocket(port);
      serverSocket.setReuseAddress(true);
      // Wait for connection from client.
      while (true) {
        clientSocket = serverSocket.accept();
        concurrentTasks.put(new AsyncConnectionHandler(executorService, new ConnectionHandler(new Connection(clientSocket))));
      }
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      try {
        if (serverSocket != null) {
          serverSocket.close();
        }
      } catch (IOException e) {
        System.out.println("IOException: " + e.getMessage());
      }
    }
  }
  public static void main(String args) {
    ArrayList<String> incomingPingCommandValid = new ArrayList<>();
    ArrayList<String> incomingPingCommandWithArgument = new ArrayList<>();
    ArrayList<String> incomingPingCommandInvalid = new ArrayList<>();
    incomingPingCommandValid.add("*1");
    incomingPingCommandValid.add("$4");
    incomingPingCommandValid.add("PING");
    // *2..$4..PING..$4..Test..
    incomingPingCommandWithArgument.add("*2");
    incomingPingCommandWithArgument.add("$4");
    incomingPingCommandWithArgument.add("PING");
    incomingPingCommandWithArgument.add("$4");
    incomingPingCommandWithArgument.add("Test");
    // *3..$4..PING..$4..Yay,..$1..1.
    incomingPingCommandInvalid.add("*3");
    incomingPingCommandInvalid.add("$4");
    incomingPingCommandInvalid.add("PING");
    incomingPingCommandInvalid.add("$3");
    incomingPingCommandInvalid.add("Yay");
    incomingPingCommandInvalid.add("$1");
    incomingPingCommandInvalid.add("1");

    ICommand validCommand = IRespProtocol.decode(incomingPingCommandValid);
    ICommand validCommandWithArgument = IRespProtocol.decode(incomingPingCommandWithArgument);
    ICommand invalidCommand = IRespProtocol.decode(incomingPingCommandInvalid);

    if (validCommand.getMessage().isPresent()) {
      System.out.println(validCommand.getMessage().get());
    } else if (validCommand.getError().isPresent()) {
      System.out.println("-" + validCommand.getError().get());
    } else {
      System.out.println("PONG");
    }
    if (validCommandWithArgument.getMessage().isPresent()) {
      System.out.println(validCommandWithArgument.getMessage().get());
    } else if (validCommandWithArgument.getError().isPresent()) {
      System.out.println("-" + validCommandWithArgument.getError().get());
    } else {
      System.out.println("PONG");
    }
    if (invalidCommand.getMessage().isPresent()) {
      System.out.println(validCommand.getMessage().get());
    } else if (invalidCommand.getError().isPresent()) {
      System.out.println("-" + invalidCommand.getError().get());
    } else {
      System.out.println("PONG");
    }
  }
}
