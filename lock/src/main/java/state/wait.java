package state;

/**
 * @author mawt
 * @description
 * @date 2020/1/6
 */
public class wait {

    //对象锁
    private static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            synchronized (lock) {
                try {
                    System.out.println(1);
                    //当前线程调用对象的wait()方法，当前线程释放对象锁，进入等待队列WAITING
                    lock.wait();
                    System.out.println(2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        Thread.sleep(2000);

        Thread thread1 = new Thread(() -> {
            synchronized (lock) {
                //WAITING
                System.out.println(thread.getState().name());
                lock.notifyAll();

            }
        });
        thread1.start();
    }

}
