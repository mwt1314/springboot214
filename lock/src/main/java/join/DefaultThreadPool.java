package join;

import java.util.LinkedList;

/**
 * @author mawt
 * @description
 * @date 2020/6/5
 */
public class DefaultThreadPool<Job extends Runnable> implements ThreadPool<Job> {

    private int poolSize;

    private LinkedList<Thread> threadList;  //所有工作线程

    private LinkedList<Job> jobList;    //具体的任务

    private LinkedList<Worker> workerList;

    public static void main(String[] args) {
        DefaultThreadPool pool = new DefaultThreadPool(5);
        for (int i = 0; i < 20; i++) {
            pool.execute(() -> {
                /*try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                System.out.println(Thread.currentThread().getName() + "执行");
            });
        }
        pool.shutdown();
    }

    public DefaultThreadPool(int poolSize) {
        this.poolSize = poolSize;
        this.threadList = new LinkedList<>();
        this.jobList = new LinkedList<>();
        this.workerList = new LinkedList<>();
        for (int i = 0; i < poolSize; i++) {
            Worker worker = new Worker();
            workerList.add(worker);
            Thread thread = new Thread(worker, "thread-" + i);
            threadList.add(thread);
            thread.start();
        }
    }

    @Override
    public void execute(Job job) {
        synchronized (jobList) {
            jobList.add(job);
            jobList.notifyAll();
        }
    }

    @Override
    public void shutdown() {
        while (!jobList.isEmpty()) {
            System.out.println(getJobSize());
        }
        for (Worker worker : workerList) {
            worker.setStop(true);
        }
        synchronized (jobList) {
            jobList.notifyAll();
        }

        System.out.println("finish");
    }

    @Override
    public void shutdownNow() {
        for (Worker worker : workerList) {
            worker.setStop(true);
        }
        for (Thread thread : threadList) {
            thread.interrupt();
        }
    }

    @Override
    public void addWorkers(int num) {

    }

    @Override
    public void removeWorker(int num) {

    }

    @Override
    public int getJobSize() {
        return jobList.size();
    }

    class Worker implements Runnable {

        boolean stop = false;

        @Override
        public void run() {
            while (!stop) {
                Job job = null;
                synchronized (jobList) {
                    while (jobList.isEmpty()) {//如果没有任务，就让线程等待
                        try {
                            System.out.println("等待");
                            jobList.wait(); //等待被唤醒
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            if (stop) break;
                        }
                    }
                    job = jobList.removeFirst();
                }
                if (job != null) {
                    job.run();
                }
                //Thread.yield();
            }
        }

        public void setStop(boolean stop) {
            this.stop = stop;
        }
    }

}
