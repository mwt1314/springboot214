package falsesharing;

/**
 * @author mawt
 * @description
 * @date 2020/5/14
 */
public class Test {

    public static void main(String[] args) throws InterruptedException {
        Pointer pointer = new Pointer();
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 100000000; i++) {
                pointer.x++;
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 100000000; i++) {
                pointer.y++;
            }
        });
        long begin = System.currentTimeMillis();
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println(System.currentTimeMillis() - begin);
        System.out.println(pointer.x + "," + pointer.y);
    }

    private static class Pointer {
        volatile long x;
        long p1, p2, p3, p4, p5, p6, p7;
        volatile long y;
    }

}
