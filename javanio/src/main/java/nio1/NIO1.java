package nio1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIO1 {
    public static void main(String[] args) throws Exception{
        // 1. 获取数据源 和 目标传输地的输入输出流（此处以数据源 = 文件为例）
        String infile = "";
        String outfile = "";
        FileInputStream fin = new FileInputStream(infile);
        FileOutputStream fout = new FileOutputStream(outfile);

        // 2. 获取数据源的输入输出通道
        FileChannel fcin = fin.getChannel();
        FileChannel fcout = fout.getChannel();

        // 3. 创建 缓冲区 对象：Buffer（共有2种方法）
        // 方法1：使用allocate()静态方法
        ByteBuffer buff = ByteBuffer.allocate(256);
        // 上述方法创建1个容量为256字节的ByteBuffer
        // 注：若发现创建的缓冲区容量太小，则重新创建一个大小合适的缓冲区

        // 方法2：通过包装一个已有的数组来创建
        // 注：通过包装的方法创建的缓冲区保留了被包装数组内保存的数据
        //ByteBuffer buff = ByteBuffer.wrap(byteArray);

        // 额外：若需将1个字符串存入ByteBuffer，则如下
        String sendString="你好,服务器. ";
        ByteBuffer sendBuff = ByteBuffer.wrap(sendString.getBytes("UTF-16"));

        // 4. 从通道读取数据 & 写入到缓冲区
        // 注：若 以读取到该通道数据的末尾，则返回-1
        fcin.read(buff);

        // 5. 传出数据准备：将缓存区的写模式 转换->> 读模式
        buff.flip();

        // 6. 从 Buffer 中读取数据 & 传出数据到通道
        fcout.write(buff);

        // 7. 重置缓冲区
        // 目的：重用现在的缓冲区,即 不必为了每次读写都创建新的缓冲区，在再次读取之前要重置缓冲区
        // 注：不会改变缓冲区的数据，只是重置缓冲区的主要索引值
        buff.clear();
    }

}
