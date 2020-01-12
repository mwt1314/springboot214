package state;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

/**
 * @author mawt
 * @description
 * @date 2020/1/6
 */
public class notifyAll {

    private static final Object lock = new Object();

    public static void main(String[] args) {
        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                synchronized (lock) {
                    try {
                        //释放对象锁，当前线程进入WAITING状态，等待唤醒
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName());
                }
            });
            LockSupport.park();
            thread.start();
            threadList.add(thread);
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadList.forEach( t -> System.out.println("线程状态为：" + t.getState().name()));

        new Thread(() -> {
            synchronized (lock) {
                //唤醒对象锁等待池中的全部线程
                lock.notifyAll();
                threadList.forEach( t -> System.out.println("线程状态为：" + t.getState().name()));
            }
        }).start();
    }

}
