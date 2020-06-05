package join;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author mawt
 * @description
 * @date 2020/6/5
 */
public class EDU {

    static private AtomicInteger num = new AtomicInteger(0);

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            new Thread(new Worker(i, 3), "Thread-" + i).start();
        }
    }

    private static class Worker implements Runnable {
        private int index, max;

        public Worker(int index, int max) {
            this.index = index;
            this.max = max;
        }

        @Override
        public void run() {
            if (index == 0) {
                //第一个线程
                while (max > 0) {
                    if (num.get() == 0) {
                        System.out.print("E");
                        max--;
                        num.incrementAndGet();
                    }
                }
            } else if (index == 1) {
                //第二个线程
                while (max > 0) {
                    if (num.get() == 1) {
                        System.out.print("D");
                        max--;
                        num.incrementAndGet();
                    }
                }
            } else {
                //第三个线程
                while (max > 0) {
                    if (num.get() == 2) {
                        System.out.print("U");
                        max--;
                        num.getAndAdd(-2);
                    }
                }
            }
        }

    }

}
