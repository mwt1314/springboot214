package interrupt;

public class ThreadBreak3 implements Runnable {

    @Override
    public void run() {
        for(int i = 0 ; i < 10000; i ++){
            System.out.println(i);
        }
        try {
            System.out.println("开始sleep");

            //如果在sleep之前该线程的中断标志置为true了，那么也会在sleep的时候抛出异常java.lang.InterruptedException: sleep interrupted
            Thread.sleep(20000);
            System.out.println("结束sleep");
        } catch (InterruptedException e) {
            System.out.println("如果输出，那就被捕获了");
            //这里打印出来的中断标记为false,因为只要进入了InterruptedException异常，中断标记就会被清除掉
            System.out.println("中断标记为:" + Thread.currentThread().isInterrupted());
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Thread thread = new Thread(new ThreadBreak3());
        thread.start();

        //将中断标志置true
        thread.interrupt();
        System.out.println("已经将线程中断标志置为true了");
    }
}