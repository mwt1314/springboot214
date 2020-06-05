package threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author mawt
 * @description
 * @date 2020/6/5
 */
public class ThreadPoolTest {

    public static void main(String[] args) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            executor.execute(() -> {
                System.out.println(Thread.currentThread());
            });
        }

    }

}
