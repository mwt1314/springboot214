package interrupt;

/**
 * @author mawt
 * @description
 * @date 2020/1/7
 */
public class InterruptSleepMain implements Runnable {

    @Override
    public void run() {
        try {
            System.out.println("线程sleep start...");
            Thread.sleep(1000);
            System.out.println("线程sleep end");
        } catch (InterruptedException e) {
            //如果在sleep()的过程中调用了interrupt()方法，就会进入这里，因为会强行中断sleep()

            //这里打印出来的中断标记为false,因为只要进入了InterruptedException异常，中断标记就会被清除掉
            System.out.println("中断标记为:" + Thread.currentThread().isInterrupted());
            System.out.println("我把中断捕获了...");
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Thread thread = new Thread(new InterruptSleepMain());
        thread.start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();
        System.out.println("中断线程了");

    }

}
