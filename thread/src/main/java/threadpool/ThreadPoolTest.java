package threadpool;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPoolTest {

    public static void main(String[] args) throws InterruptedException {
        // 创建3个线程的线程池
        MyThreadPool t = new MyThreadPool(3);
        CountDownLatch countDownLatch = new CountDownLatch(5);
        t.execute(new MyTask(countDownLatch, "testA"));
        t.execute(new MyTask(countDownLatch, "testB"));
        t.execute(new MyTask(countDownLatch, "testC"));
        t.execute(new MyTask(countDownLatch, "testD"));
        t.execute(new MyTask(countDownLatch, "testE"));
        countDownLatch.await();
        Thread.sleep(500);
        t.shutdown();// 所有线程都执行完成才destory
        System.out.println("finished...");

        // 1000 0000 0000 0000 0000 0000 0000 0001

        System.out.println(-1 << 29);
        System.out.println(Integer.toBinaryString(1 << 29));
        System.out.println("........");
        System.out.println(Integer.toBinaryString((1 << 29) -1));

        /*String s = Integer.toBinaryString(-1 << 29);
        System.out.println(Integer.toBinaryString(-1));
        System.out.println(Integer.toBinaryString(-2));
        System.out.println(Integer.toBinaryString(2));
        System.out.println(s);*/

        //使用默认thread工厂
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        executorService.execute(null);


        //预创建线程的意思，即在没有任务到来之前就创建corePoolSize个线程或者一个线程
        //默认情况下，在创建了线程池后，线程池中的线程数为0，当有任务来之后，就会创建一个线程去执行任务，
        //当线程池中的线程数目达到corePoolSize后，就会把到达的任务放到缓存队列当中
        executorService.prestartAllCoreThreads();
        executorService.prestartCoreThread();


        Executors.newFixedThreadPool(6, new MyThreadFactory());

        ExecutorService executorService1 = Executors.newSingleThreadExecutor();

    }

    // 任务类
    static class MyTask implements Runnable {

        private CountDownLatch countDownLatch;
        private String name;
        private Random r = new Random();

        public MyTask(CountDownLatch countDownLatch, String name) {
            this.countDownLatch = countDownLatch;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public void run() {// 执行任务
            try {
                countDownLatch.countDown();
                Thread.sleep(r.nextInt(1000));
                System.out.println("任务 " + name + " 完成，" + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getId() + " sleep InterruptedException:"
                        + Thread.currentThread().isInterrupted());
            }
        }
    }
}