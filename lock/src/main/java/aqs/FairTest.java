package aqs;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author mawt
 * @description
 * @date 2020/5/27
 */
public class FairTest {

    private static ReentrantLock lock;

    public static void main(String[] args) {
        int x = 1;
        if (x == 1) {
            x = 2;
        } else if(x == 2) {
            x = 3;
        }
        System.out.println(x);

        lock = new ReentrantLock(true); //公平锁
        Task task = new Task();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(task);
            thread.start();
        }
    }

    private static class Task implements Runnable {

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " is running");
            try {
                lock.lock();
                int queueLength = lock.getQueueLength();
                System.out.println(Thread.currentThread().getName() + " success locked " + lock.getHoldCount() + "\t" + queueLength);
            } finally {
                lock.unlock();
            }
        }
    }


}
