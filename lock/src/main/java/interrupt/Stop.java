package interrupt;

/**
 * @author mawt
 * @description
 * @date 2020/1/7
 */
public class Stop implements Runnable {

    @Override
    public void run() {
        System.out.println("进入线程");
        try {
            Thread.sleep(20000);
            System.out.println("结束线程");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Thread t = new Thread(new Stop());
        t.start();

        try {
            Thread.sleep(1000);

            //stop线程
            t.stop();

            System.out.println("over");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
