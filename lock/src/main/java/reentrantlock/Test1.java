package reentrantlock;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author mawt
 * @description
 * @date 2020/1/9
 */
public class Test1 {

    private static volatile int x = 0;

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        java.util.concurrent.locks.ReentrantLock rtl = new ReentrantLock(); //默认非公平锁
        Field syncField = rtl.getClass().getDeclaredField("sync");
        syncField.setAccessible(true);

        Class<?> syncClazz = Class.forName("java.util.concurrent.locks.ReentrantLock$Sync");
        Field stateField = syncClazz.getSuperclass().getDeclaredField("state");
        stateField.setAccessible(true);

        //固定线程为9条，复用
        ExecutorService executors = Executors.newFixedThreadPool(9);
        for (int i = 0; i < 1000; i++) {
            executors.execute(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + "线程lock之后state=" + stateField.get(syncField.get(rtl)));
                    //
                    rtl.lock();
                    x++;
                    Thread.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    rtl.unlock();
                }
            });
        }
        executors.shutdown();
        while (!executors.isTerminated()) {

        }
        System.out.println(x);

    }

}
