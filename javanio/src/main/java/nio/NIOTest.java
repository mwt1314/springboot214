package nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author mawt
 * @description
 * @date 2020/5/21
 */
public class NIOTest {

    public static void main(String[] args) throws IOException {
    //    readIo();
        readFileByNIO();
    }

    private static void readFileByNIO() throws IOException {
        File file = new File("E:\\rocketmq-externals-master\\rocketmq-console\\target/run.bat");
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        FileChannel channel = raf.getChannel();
        ByteBuffer buf = ByteBuffer.allocate(1024); //分配空间
        int len;
        StringBuilder content = new StringBuilder(512);
        while ((len = channel.read(buf)) != -1) {   //写入数据到buf
            buf.flip();
            content.append(new String(buf.array(), 0, len));

            while (buf.hasRemaining()) {
                System.out.print((char) buf.get());
            }
            buf.compact();  //清空buf ，buf.clear();
        }
        channel.close();
        raf.close();
        System.out.println(content);
    }

    private static void readIo() throws IOException {
        File file = new File("E:\\rocketmq-externals-master\\rocketmq-console\\target/run.bat");
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        byte[] buf = new byte[1024];
        int len = 0;
        StringBuilder content = new StringBuilder();
        while ((len = bis.read(buf)) != -1) {
            content.append(new String(buf, 0, len, "UTF-8"));
        }
        bis.close();
        System.out.println(content);
    }

}
