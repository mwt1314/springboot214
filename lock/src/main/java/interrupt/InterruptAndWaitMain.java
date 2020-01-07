package interrupt;

/**
 * @author mawt
 * @description
 * @date 2020/1/7
 */
public class InterruptAndWaitMain {

    private static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                try {
                    lock.wait();
                    //该方法返回当前线程的内部中断状态，然后清除中断状态（置为false）
                    boolean interrupted = Thread.interrupted();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("wait结束");
            }
        });
        t1.start();
        Thread.sleep(1000);
        System.out.println(t1.isInterrupted());
        t1.interrupt();
    }
}
