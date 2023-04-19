import com.redis.protocol.IRespProtocol;
import com.redis.protocol.command.ICommand;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.*;

public class Main {

  private static final String POISON_PILL = "POISON_PILL";
  public static void main(String[] args) throws IOException {
      ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
      serverSocketChannel.socket().bind(new InetSocketAddress(6379));
      serverSocketChannel.configureBlocking(false);
      Selector selector = Selector.open();
      serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
      while (true) {
          selector.select();
          Set<SelectionKey> selectionKeySet = selector.selectedKeys();
          Iterator<SelectionKey> iterator = selectionKeySet.iterator();
          while (iterator.hasNext()) {
              SelectionKey selectionKey = iterator.next();
              iterator.remove();
              if (selectionKey.isAcceptable()) {
                  handleNewConnection(selector, serverSocketChannel);
              }
              if (selectionKey.isReadable()) {
                  handleRequest(selectionKey);
              }
          }
      }
}

private static void handleNewConnection(Selector selector, ServerSocketChannel serverSocketChannel) throws IOException {
      SocketChannel client = serverSocketChannel.accept();
      System.out.println(client);
      client.configureBlocking(false);
      client.register(selector, SelectionKey.OP_READ);
}

private static void handleRequest(SelectionKey selectionKey) throws IOException {
      ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
      SocketChannel client = (SocketChannel) selectionKey.channel();
      int remainingByte = client.read(byteBuffer);
      if (remainingByte == -1 || new String(byteBuffer.array()).trim().equals(POISON_PILL)) {
          client.close();
      } else {
          byteBuffer.flip();
          ICommand command = IRespProtocol.decode(new ArrayList<>(Arrays.asList(new String(byteBuffer.array()).split("\r\n"))));
          String response = command.generateResponse();
          System.out.println(new String(byteBuffer.array()));
          System.out.println(response);
          byteBuffer.clear();
          client.write(ByteBuffer.wrap(response.getBytes(Charset.defaultCharset())));
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
