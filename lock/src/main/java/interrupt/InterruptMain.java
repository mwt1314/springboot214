package interrupt;

/**
 * @author mawt
 * @description
 * @date 2020/1/7
 */
public class InterruptMain {

    //如果线程处于sleep时被中断，会抛出异常java.lang.InterruptedException: sleep interrupted
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                System.out.println("t1 sleep 2s");
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t1.start();

        Thread.sleep(1000);
        System.out.println("中断t1");
        t1.interrupt();

    }

}
