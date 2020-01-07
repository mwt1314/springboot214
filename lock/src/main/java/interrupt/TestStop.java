package interrupt;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author mawt
 * @description
 * @date 2020/1/7
 */
public class TestStop {

    private static final int[] array = new int[8000000];

    static {
        Random random = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(i + 1);
        }
    }

    private static final Thread t = new Thread(() -> {
        try {
            System.out.println(sort(array));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("in thread t");

    });

    private static int sort(int[] array) {
        int length = array.length;
        for (int i = 0; i < length - 1; i++) {
            for (int j = 0; j < length - i - 1; j++) {
                if (array[j] < array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
        return array[0];
    }

    public static void main(String[] args) throws InterruptedException {
        t.start();
        Thread.sleep(10);
    //    TimeUnit.SECONDS.sleep(1);
        System.out.println("go to stop thread t");
        t.stop();
        System.out.println("finish main");
    }

}
