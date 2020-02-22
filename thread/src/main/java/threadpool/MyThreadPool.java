package threadpool;

import java.util.LinkedList;
import java.util.List;

public class MyThreadPool {

    //线程池中允许的最大线程数
    private static int MAXTHREDNUM = Integer.MAX_VALUE;
    //当用户没有指定时默认的线程数
    private int threadNum = 6;
    //线程队列，存放线程任务
    private List<Runnable> queue;

    private WorkerThread[] workerThreads;

    public MyThreadPool(int threadNum) {
        this.threadNum = threadNum;
        if (threadNum > MAXTHREDNUM)
            threadNum = MAXTHREDNUM;
        this.queue = new LinkedList<>();
        this.workerThreads = new WorkerThread[threadNum];
        init();
    }

    //初始化线程池中的线程
    private void init() {
        for (int i = 0; i < threadNum; i++) {
            workerThreads[i] = new WorkerThread();
            workerThreads[i].start();
        }
    }


    //销毁线程池
    public void shutdown() {
        for (int i = 0; i < threadNum; i++) {
            workerThreads[i].cancel();
            workerThreads[i] = null;
        }
        queue.clear();
    }

    //提交任务
    public void execute(Runnable task) {
        synchronized (queue) {
            queue.add(task);
            //提交任务后唤醒等待在队列的线程
            queue.notifyAll();
        }
    }

    private class WorkerThread extends Thread {

        private volatile boolean on = true;

        @Override
        public void run() {
            Runnable task = null;
            //判断是否可以取任务
            try {
                while (on && !isInterrupted()) {
                    synchronized (queue) {
                        while (on && !isInterrupted() && queue.isEmpty()) {
                            //这里如果使用阻塞队列来获取在执行时就不会报错
                            //报错是因为退出时销毁了所有的线程资源，不影响使用
                            queue.wait(1000);
                        }
                        if (on && !isInterrupted() && !queue.isEmpty()) {
                            task = queue.remove(0);
                        }

                        if (task != null) {
                            //取到任务后执行
                            task.run();
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            task = null;//任务结束后手动置空，加速回收
        }

        public void cancel() {
            on = false;
            interrupt();
        }
    }

}
