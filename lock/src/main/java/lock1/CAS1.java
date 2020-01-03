package lock1;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author mawt
 * @description
 * @date 2020/1/3
 */
public class CAS1 {

    static AtomicInteger ai = new AtomicInteger(0);
    public static void main(String[] args) throws Exception {
        CountDownLatch count = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {ai.getAndIncrement(); count.countDown();}).start();
        }
        count.await();
        System.out.println(ai.get());
    }

}
