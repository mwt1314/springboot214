package state;

import lombok.SneakyThrows;

/**
 * @author mawt
 * @description
 * @date 2020/1/6
 */
public class waittime {

    private static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                synchronized (lock) {
                    System.out.println(1);
                    //当前线程调用对象的wait()方法，当前线程释放对象锁，进入等待队列WAITING
                    try {
                        lock.wait(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(2);
                }
            }
        });
        thread.start();

        //   Thread.sleep(2000);


        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                String name = thread.getState().name();
                System.out.println(name);
                lock.notifyAll();
            }
        });

        thread1.start();

    }

}
