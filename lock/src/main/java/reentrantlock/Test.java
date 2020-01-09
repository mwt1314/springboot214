package reentrantlock;

import java.lang.reflect.Field;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author mawt
 * @description
 * @date 2020/1/9
 */
public class Test {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        ReentrantLock rtl = new ReentrantLock(); //默认非公平锁
        Field syncField = rtl.getClass().getDeclaredField("sync");
        syncField.setAccessible(true);

        Class<?> syncClazz = Class.forName("java.util.concurrent.locks.ReentrantLock$Sync");
        Field stateField = syncClazz.getSuperclass().getDeclaredField("state");
        stateField.setAccessible(true);

        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                try {
                    rtl.lock();
                    System.out.println("state=" + stateField.get(syncField.get(rtl)));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    rtl.unlock();
                }
            }).start();
        }

    }

}
