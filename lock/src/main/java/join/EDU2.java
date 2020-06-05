package join;

import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author mawt
 * @description
 * @date 2020/6/5
 */
public class EDU2 {

    static Object lock1 = new Object();

    static Object lock2 = new Object();

    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            new Thread(new Worker(i, 3), "Thread-" + i).start();
        }
    }

    private static class Worker implements Runnable {
        private int index, max;

        public Worker(int index, int max) {
            this.index = index;
            this.max = max;
        }

        @SneakyThrows
        @Override
        public void run() {
            if (index == 0) {
                //第一个线程
                while (max > 0) {
                    synchronized (lock1) {
                        System.out.print("E");
                        max--;
                        lock1.notify();
                    }

                }
            } else if (index == 1) {
                //第二个线程
                while (max > 0) {
                    synchronized (lock1) {
                        System.out.println("等待中");
                        lock1.wait();
                        System.out.print("D");
                        max--;
                    }

                }
            } else {
                //第三个线程

            }
        }

    }

}
