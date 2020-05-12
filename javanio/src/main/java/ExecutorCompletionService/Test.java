package ExecutorCompletionService;

import java.util.Iterator;
import java.util.concurrent.*;

/**
 * @author mawt
 * @description
 * @date 2020/5/11
 */
public class Test {

    private static int cpus = Runtime.getRuntime().availableProcessors();

    public static final int DEF_MAX_THREADS_PER_CPU = 8;

    public static void main(String[] args) {
        int nThreads = (cpus * DEF_MAX_THREADS_PER_CPU) / 2 + 1;
        ThreadPoolExecutor executor = new ThreadPoolExecutor(nThreads, nThreads, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

        ExecutorCompletionService<String> service = new ExecutorCompletionService<>(executor);

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                int index = i;
                service.submit(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return "task done" + index;
                    }
                });
            }

        }).start();

        // 消费者
        new Thread() {
            @Override
            public void run() {
                try {
                    Future<String> take = service.take();
                    // do some thing........


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    private static void xx() throws InterruptedException {
        // 队列
        BlockingQueue<Future<String>> futures = new LinkedBlockingQueue<>();
        Thread thread = new Thread(() -> {
            ExecutorService pool = Executors.newCachedThreadPool();

            for (int i = 0; i < 10; i++) {
                int index = i;
                Future<String> submit = pool.submit(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return "task done" + index;
                    }
                });
                try {
                    futures.put(submit);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        thread.join();
        // 消费者
        new Thread(() -> {
            while(true) {
                Iterator<Future<String>> iterator = futures.iterator();
                while (iterator.hasNext()) {
                    Future<String> future = iterator.next();
                    if (future.isDone()) {
                        try {
                            String s = future.get();
                            System.out.println("消费" + s);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } finally {
                            iterator.remove();
                        }
                    }
                }
            }
        }).start();

    }


}
