package atomicmarkablereference;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * @author mawt
 * @description
 * @date 2020/1/8
 */
public class AtomicMarkableReferenceMain {

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
        AtomicMarkableReference<Stu> amr = new AtomicMarkableReference<>(mwt, false);
        List<Thread> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(() -> {
                while (true) {
                    Stu oldRef = amr.getReference();
                    boolean oldMarked = amr.isMarked(); //表示引用变量是否被更改过

                    Stu newRef = new Stu(oldRef.name, oldRef.age + 1);
                    if (amr.compareAndSet(oldRef, newRef, oldMarked, !oldMarked)) {
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

        Stu oldRef = amr.getReference();
        boolean[] markHolder = new boolean[1];
        Stu stu = amr.get(markHolder);
        boolean marked = amr.isMarked();

    }


}
