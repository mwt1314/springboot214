package state;

/**
 * @author mawt
 * @description
 * @date 2020/1/6
 */
public class notify {

    private static final Object lock = new Object();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                try {
                    lock.wait(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("t1");
            }
        });
        t1.start();
        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                try {
                    lock.wait(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("t2");
            }
        });
        t2.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("t1状态：" + t1.getState().name());
        System.out.println("t2状态：" + t2.getState().name());
        Thread t3 = new Thread(() -> {
            synchronized (lock) {
                lock.notify();
            }
        });
        t3.start();
    }

}
