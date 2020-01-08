package atomicreference;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author mawt
 * @description
 * @date 2020/1/8
 */
public class AtomicReferenceMain {

    public static void main(String[] args) throws InterruptedException {
        AtomicReference<Integer> ref = new AtomicReference<>(new Integer(10000));
        ArrayList<Thread> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            Thread t = new Thread(new Task(ref), "Thread-" + i);
            list.add(t);
            t.start();
        }
        for (Thread t : list) {
            t.join();
        }
        System.out.println(ref.get());    // 打印2000
    }

    static class Task implements Runnable {
        private AtomicReference<Integer> ref;

        Task(AtomicReference<Integer> ref) {
            this.ref = ref;
        }

        @Override
        public void run() {
            while (true) {
                Integer oldVal = ref.get();
                if (ref.compareAndSet(oldVal, oldVal.intValue() + 1)) {
                    break;
                } else {
                    System.out.println(Thread.currentThread().getName());
                }
            }
        }
    }

}
