package mutex;

import sun.awt.Mutex;

/**
 * @author mawt
 * @description
 * @date 2020/5/20
 */
public class MutexTest {

    private static Mutex mutex = new Mutex();

    public static void main(String[] args) {
        Task task = new Task();
        task.setTotal(10);
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(task);
            thread.start();
        }
    }

    private static class Task implements Runnable {

        private int total;

        public void setTotal(int total) {
            this.total = total;
        }

        @Override
        public void run() {
            /*try {
                mutex.lock();
                total--;
                System.out.println("卖出了第" + total + "号票");
            } finally {
                mutex.unlock();
            }*/
            synchronized (this) {
                total--;
                System.out.println("卖出了第" + total + "号票");
            }
        }
    }

    public static class Mutex {
        private boolean locked;
        private Thread owner;

        public Mutex() {
        }

        public synchronized void lock() {
            if (this.locked && Thread.currentThread() == this.owner) {
                throw new IllegalMonitorStateException();
            } else {
                do {
                    if (!this.locked) {
                        this.locked = true;
                        this.owner = Thread.currentThread();
                    } else {
                        try {
                            this.wait();    //释放对象锁，并进入等待池等待被唤醒或中断
                        } catch (InterruptedException var2) {
                        }
                    }
                } while(this.owner != Thread.currentThread());

            }
        }

        public synchronized void unlock() {
            if (Thread.currentThread() != this.owner) {
                throw new IllegalMonitorStateException();
            } else {
                this.owner = null;
                this.locked = false;
                this.notify();
            }
        }

        protected boolean isOwned() {
            return this.locked && Thread.currentThread() == this.owner;
        }
    }

}
