package join;

import java.util.concurrent.TimeUnit;

/**
 * @author mawt
 * @description
 * @date 2020/6/5
 */
public class DeadLock2 {

    static Object lock1 = new Object();

    static Object lock2 = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            synchronized (lock1) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock2) {
                    System.out.println("1");
                }
            }
        });
        thread1.start();
        TimeUnit.SECONDS.sleep(1);
        Thread thread2 = new Thread(() -> {
            synchronized (lock2) {
                synchronized (lock1) {
                    System.out.println("2");
                }
            }
        });
        thread2.start();
    }

}
