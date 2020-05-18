package park;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author mawt
 * @description
 * @date 2020/5/18
 */
public class LockTest {


    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            try {
                LockSupport.park();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(1231);
            }

            System.out.println(123);
        });

        thread.start();

        try {
            TimeUnit.SECONDS.sleep(2);
            thread.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(1);

    }

}
