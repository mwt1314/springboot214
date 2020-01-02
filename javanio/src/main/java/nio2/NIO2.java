package nio2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIO2 {

    private static int port = 8081;

    public static void main(String[] args) throws IOException {
        //创建selector对象
        Selector selector = Selector.open();

        // 2. 向Selector对象绑定通道
        // a. 创建可选择通道，并配置为非阻塞模式
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        // b. 绑定通道到指定端口
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(port));

        // c. 向Selector中注册感兴趣的事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);


        while (true) {
            // 该调用会阻塞，直到至少有一个事件就绪、准备发生
            int select = selector.select();

            // 一旦上述方法返回，线程就可以处理这些事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
            //    process(key);
            }
        }

    }

}
