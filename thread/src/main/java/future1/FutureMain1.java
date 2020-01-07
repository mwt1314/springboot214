package future1;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author mawt
 * @description
 * @date 2020/1/7
 */
public class FutureMain1 {

    public static void main(String[] args) {
        FutureTask<Object> futureTask = new FutureTask<>(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                System.out.println(1);
                return 1;
            }
        });
        Object o = null;
        futureTask.run();
        try {
            o = futureTask.get();
            System.out.println(o);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

}
