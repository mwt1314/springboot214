package atomicstempedreference;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author mawt
 * @description
 * @date 2020/1/8
 */
public class AtomicStampedReferenceMain {

    private static class Stu {
        public String name;
        public int age;

        public Stu(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Stu mwt = new Stu("mwt", 25);
        AtomicStampedReference<Stu> ref = new AtomicStampedReference(mwt, 0);
        List<Thread> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(() -> {
                while (true) {
                    Stu oldRef = ref.getReference();
                    int oldStamp = ref.getStamp();

                    Stu newRef = new Stu(oldRef.name, oldRef.age + 1);
                    if (ref.compareAndSet(oldRef, newRef, oldStamp, oldStamp + 1)) {
                        break;
                    }
                }
            });
            thread.start();
            list.add(thread);
        }

        for (Thread thread : list) {
            thread.join();
        }

        int[] stampHolder = new int[1];
        //同时返回stamp和ref
        Stu stu = ref.get(stampHolder);

        System.out.println(stu.name);
        System.out.println(stu.age);
        System.out.println(stampHolder[0]);


        System.out.println(ref.getStamp());
        System.out.println(ref.getReference().age);
    }

}
