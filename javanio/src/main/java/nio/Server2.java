package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author mawt
 * @description
 * @date 2020/5/21
 */
public class Server2 {

    public static void main(String[] args) throws IOException {
        /** 实例化一个选择器对象 **/
        Selector selector = Selector.open();

        /** 创建服务器套接字通道 ServerSocketChannel **/
        ServerSocketChannel ssc = ServerSocketChannel.open();

        //绑定端口
        ssc.socket().bind(new InetSocketAddress(8077));
        /** 绑定监听 InetSocketAddress **/
        ssc.bind(new InetSocketAddress("localhost", 8888));

        /** 设置为非阻塞IO模型 **/
        ssc.configureBlocking(false);

        //将Channel注册到Selector上,
        // 与Selector一起使用时，Channel必须处于非阻塞模式下。
        // 这意味着不能将FileChannel与Selector一起使用，
        // 因为FileChannel不能切换到非阻塞模式。而套接字通道都可以。
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        Object obj = new Object();
        /** 将serverSocketChannel通道注册到selector选择器中，并设置感兴趣的事件OP_ACCEPT**/
        SelectionKey register = ssc.register(selector, SelectionKey.OP_ACCEPT, obj);//在用register()方法向Selector注册Channel的时候附加对象
        //ops参数：在通过Selector监听Channel时对什么事件感兴趣。可以监听四种不同类型的事件：
        //ACCEPT
        //CONNECT
        //WRITE
        //READ
        //通道触发了一个事件意思是该事件已经就绪。所以，
        // 某个channel成功连接到另一个服务器称为“连接就绪”。
        // 一个server socket channel准备好接收新进入的连接称为“接收就绪”。
        // 一个有数据可读的通道可以说是“读就绪”。
        // 等待写数据的通道可以说是“写就绪”。


        /** 获取通道SelectionKey对象中的通道 **/
        System.out.println(register.channel().equals(ssc));
        /** 获取通道SelectionKey对象绑定到选择器中的附件对象**/
        System.out.println(register.attachment().equals(obj));
        /** 获取通道SelectionKey对象中选择器 **/
        System.out.println(register.selector().equals(selector));
        /** 获取通道SelectionKey对象中感兴趣的事件 **/
        System.out.println((register.interestOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT);

        while (true) {
            //通过selector选择通道
            //select()阻塞到至少有一个通道在你注册的事件上就绪了。返回的int值表示有多少通道准备就绪
            //select(long timeout)和select()一样，除了最长会阻塞timeout毫秒(参数)。
            //selectNow()不会阻塞，不管什么通道就绪都立刻返回（译者注：此方法执行非阻塞的选择操作。如果自从前一次选择操作后，没有通道变成可选择的，则此方法直接返回零。）。
            if (selector.select(3000) == 0) {
                System.out.println("==");
                continue;
            }
            /** 获取到达事件SelectionKey集合**/
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();    //这个对象代表了注册到该Selector的通道

                //可以将一个对象或者更多信息附着到SelectionKey上，这样就能方便的识别某个给定的通道
                Object attachment = selectionKey.attachment();


                if (selectionKey.isReadable()) {

                }
                if (selectionKey.isConnectable()) {
                    System.out.println("isConnectable = true");
                }
                if (selectionKey.isAcceptable()) {
                    /** 从SelectionKey获取对应通道ServerSocketChannel**/
                    ssc = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel accept = ssc.accept();
                    /** 获取通道SelectionKey对象中就绪的事件 **/
                    System.out.println((register.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT);
                }
                if (selectionKey.isWritable()) {

                }
                //Selector不会自己从已选择键集中移除SelectionKey实例。
                // 必须在处理完通道时自己移除。下次该通道变成就绪时，Selector会再次将其放入已选择键集中
                iterator.remove();
            }
        }
    }
}
