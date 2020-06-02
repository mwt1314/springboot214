package unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author mawt
 * @description
 * @date 2020/6/1
 */
public class CASTest implements Runnable {

    transient volatile int table;

    private transient volatile int sizeCtl;

    private static Unsafe U = getUnsafe();

    private static long SIZECTL;

    static {
        try {
            SIZECTL = U.objectFieldOffset(CASTest.class.getDeclaredField("sizeCtl"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        int size, t;
        while ((t = table) == 0) {
            if ((size = sizeCtl) < 0) {
                //    System.out.println(Thread.currentThread().getName() + " waiting " + size);
                //    Thread.yield();
            } else if (U.compareAndSwapInt(this, SIZECTL, size, -1)) {
                if ((t = table) == 0) {
                    System.out.println(Thread.currentThread().getName());
                    sizeCtl = 8;
                    table = -1;
                }
                break;
            }
        }
    }

    private static int RESIZE_STAMP_BITS = 16;

    static final int resizeStamp(int n) {
        //                                       1000 0000 0000 0000
        return Integer.numberOfLeadingZeros(n) | (1 << (RESIZE_STAMP_BITS - 1));
    }

    public static void main(String[] args) {
        ConcurrentHashMap<Integer, Object> map = new ConcurrentHashMap<>();
        CountDownLatch countDownLatch = new CountDownLatch(200);
        for (int i = 0; i < 200; i++) {
            final int I = i;
            Thread thread = new Thread(() -> {
                try {
                    for (int j = I * 100; j < (2 * I * 100); j++) {
                        map.put(j, j);
                    }
                } finally {
                    countDownLatch.countDown();
                }
            });
            thread.start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(map.size());


        System.out.println();
        System.out.println(2 & (~2));
        int x = ~2;
        // 1  0000    0010
        // 1  1111    1110
        System.out.println(Integer.toBinaryString(-2));
        System.out.println(x);
        int a=2;
        System.out.println(Integer.parseUnsignedInt("11111111111111111111111111111101", 10));
        System.out.println(Integer.toBinaryString(~2));
        System.out.println("a 非的结果是："+(~a));
        System.out.println(Integer.numberOfLeadingZeros(16));
        System.out.println(Integer.toBinaryString(Integer.numberOfLeadingZeros(16)));
        System.out.println(Integer.toBinaryString(resizeStamp(16)));

        CASTest test = new CASTest();
        for (int i = 0; i < 40; i++) {
            Thread thread = new Thread(test);
            thread.start();
        }
    }

    private static Unsafe getUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
