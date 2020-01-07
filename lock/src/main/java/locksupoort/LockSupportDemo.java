package locksupoort;

import java.util.concurrent.locks.LockSupport;

/**
 * @author mawt
 * @description
 * @date 2020/1/7
 */
public class LockSupportDemo {

    public static void main(String[] args) {
        Thread mainThread = Thread.currentThread();
        Runnable runnable = () -> {
          try {
              Thread.sleep(5000);
              System.out.println("子线程进行unpark操作！");
              LockSupport.unpark(mainThread);
          } catch (Exception e) {
              e.printStackTrace();
          }
        };
        new Thread(runnable).start();
        System.out.println("开始阻塞");
        LockSupport.park(mainThread);
        System.out.println("结束阻塞！");
    }

}
