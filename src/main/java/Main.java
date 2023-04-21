import com.redis.protocol.IRespProtocol;
import com.redis.protocol.command.ICommand;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Arrays;

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

    /**
     *
     * @param selector
     * @param serverSocketChannel
     * @throws IOException
     */
    private static void handleNewConnection(Selector selector, ServerSocketChannel serverSocketChannel) throws IOException {
        SocketChannel client = serverSocketChannel.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }

    /**
     *
     * @param selectionKey
     * @throws IOException
     */
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
            byteBuffer.clear();
            client.write(ByteBuffer.wrap(response.getBytes(Charset.defaultCharset())));
        }
    }
}
