package unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

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
