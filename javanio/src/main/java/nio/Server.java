package nio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * @author mawt
 * @description
 * @date 2020/5/21
 */
public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8077);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            SocketAddress clientAddress = clientSocket.getRemoteSocketAddress();
            System.out.println("Handling client at " + clientAddress);
            byte[] recvBuf = new byte[1024];
            int recvMsgSize = 0;
            InputStream is = clientSocket.getInputStream();
            while ((recvMsgSize = is.read(recvBuf)) != -1) {
                byte[] temp = new byte[recvMsgSize];
                System.arraycopy(recvBuf, 0, temp, 0, recvMsgSize);
                System.out.println(new String(temp));
            }


        }
    }

}
