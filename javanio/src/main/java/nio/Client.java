package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * @author mawt
 * @description
 * @date 2020/5/21
 */
public class Client {

    public static void main(String[] args) throws IOException, InterruptedException {
        //客户端代码示例
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("localhost", 8080));

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        if (socketChannel.finishConnect()) {
            int i = 0;
            while (true) {
                TimeUnit.SECONDS.sleep(1);
                String info = "I'm " + i++ + "-th information from client";
                buffer.clear();
                buffer.put(info.getBytes());
                buffer.flip();
                while (buffer.hasRemaining()) {
                    System.out.println(buffer);
                    socketChannel.write(buffer);
                }

            }
        }
        socketChannel.close();
    }
}
