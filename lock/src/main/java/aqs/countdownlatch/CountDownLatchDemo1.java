package aqs.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author mawt
 * @description
 * @date 2020/1/14
 */
public class CountDownLatchDemo1 {

    public static void main(String[] args) {
        int peopleNum = 10;
        CountDownLatch beginCountDownLatch = new CountDownLatch(1);
        CountDownLatch endCountDownLatch = new CountDownLatch(peopleNum);
        for (int i = 0; i < peopleNum; i++) {
            final int NO = i + 1;
            new Thread(() -> {
                try {
                    beginCountDownLatch.await();
                    System.out.println("No." + NO + " arrived");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    endCountDownLatch.countDown();
                }
            }).start();
        }

        System.out.println("2s后开始");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        beginCountDownLatch.countDown();
        try {
            endCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Game Over.");
    }

}
