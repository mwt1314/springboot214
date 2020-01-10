package longaddr;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author mawt
 * @description 相关文档：https://www.cnblogs.com/tong-yuan/p/LongAdder.html
 * @date 2020/1/8
 */
public class LongAdderMain {

    /**
     * 在并发量较低的环境下，线程冲突的概率比较小，自旋的次数不会很多。
     * 但是，高并发环境下，N个线程同时进行自旋操作，会出现大量失败并不断自旋的情况，此时AtomicLong的自旋会成为瓶颈。
     * <p>
     * 这就是LongAdder引入的初衷——解决高并发环境下AtomicLong的自旋瓶颈问题。特别是写多的场景
     *
     * LongAdder的原理是，在最初无竞争时，只更新base的值，当有多线程竞争时通过分段的思想，让不同的线程更新不同的段，
     *  最后把这些段相加就得到了完整的LongAdder存储的值。
     *
     * @param args
     */
    public static void main(String[] args) {
        LongAdder longAdder = new LongAdder();

        ExecutorService executors = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 1000000; i++) {
            executors.submit(() -> {
                longAdder.increment();
            });
        }
        executors.shutdown();

        while (!executors.isTerminated()) {

        }
        System.out.println(longAdder.sum());

    }

    /*public void add(long x) {
        Cell[] as; long b, v; int m; Cell a;

        if ((as = cells) != null    //cells为空，需要进行cas；不为空表示已经出现了竞争，已经创建了cells
                || !casBase(b = base, b + x)) {  //如果cas失败返回false，说明其他线程抢先一步修改了base，正在出现竞争

            //true表示当前竞争还不激烈
            //false表示竞争激烈，多个线程hash到同一个cell，可能要扩容
            boolean uncontended = true;

            if (as == null      //cells为空，需要创建
                    || (m = as.length - 1) < 0
                    || (a = as[getProbe() & m]) == null //一个线程对应一个probe，它是通过随机数生成的一个值，对于一个确定的线程这个值是固定的，除非刻意修改它
                                                        //通过prope和size-1做位与运算散列到cells对应的索引位置，如果为空说明该线程所在cells中的cell为空，应初始化一个Cell
                                                        //可能会有多个线程的cells索引位置相同，存在竞争
                    || !(uncontended = a.cas(v = a.value, v + x)))  //如果执行到这里，说明当前线程对应的cell不为空，尝试更新当前cell中的value。
                                                                    //如果cell.cas成功， 也就是uncontended=true表示竞争不激烈；
                                                                    //如果失败了， 也就是uncontended=false，说明这一个cell上一定有多个线程在同步修改cell，竞争激烈
                longAccumulate(x, null, uncontended);   // 调用Striped64中的方法处理
        }
    }*/

/*
    //cpu核数，决定了槽数组的大小
    static final int NCPU = Runtime.getRuntime().availableProcessors();

    transient volatile Cell[] cells;    // cells数组，存储各个段的值

    //基数，在两种情况下会使用：
    //1.没有遇到并发竞争时，直接使用base累加数值
    //2.初始化cells数组时，必须要保证cell数组只被初始化一次（即只有一个线程可以对cells初始化），其他竞争失败的线程会将数值累加base上
    transient volatile long base;   // 最初无竞争时使用的，也算一个特殊的段

    //锁标识：
    //cells初始化或扩容时，通过cas操作将此标识设置为1-加锁状态，初始化或扩容完成后，将此标识设置为0-无锁状态
    transient volatile int cellsBusy;   // 标记当前是否有线程在创建或扩容cells，或者在创建Cell；通过CAS更新该值，相当于是一个锁

    final boolean casCellsBusy() {  //
        return UNSAFE.compareAndSwapInt(this, CELLSBUSY, 0, 1);
    }

    final void longAccumulate(long x, LongBinaryOperator fn, boolean wasUncontended) {
        int h;
        //给当前线程分配hash值
        if ((h = getProbe()) == 0) { //如果当前线程的prope为0的话
            ThreadLocalRandom.current();  // 强制初始化，防止prope还是为0
            h = getProbe(); //重新获取prope
            wasUncontended = true;   //都未初始化，肯定还不存在竞争激烈
        }
         // 是否发生碰撞
        boolean collide = false;                // True if last slot nonempty
        for (;;) {
            Cell[] as; Cell a; int n; long v; //n为cells的长度

            //case1：cells已经被初始化过了
            if ((as = cells) != null && (n = as.length) > 0) {

                if ((a = as[(n - 1) & h]) == null) {    //如果该线程对应的cell还没有被初始化的时候

                    if (cellsBusy == 0) {       // 如果现在还没有线程正在创建或扩容cells
                            // 当前无其它线程在创建或扩容cells，也没有线程在创建Cell
                        Cell r = new Cell(x);

                        if (cellsBusy == 0 && casCellsBusy()) {  // 再次检测cellsBusy，并尝试更新它为1，相当于当前线程加锁
                            boolean created = false;      // 是否创建成功
                            try {
                                Cell[] rs; int m, j;

                                 // 重新获取cells，并找到当前线程hash到cells数组中的位置
                                // 这里一定要重新获取cells，因为rs并不在锁定范围内
                                // 有可能已经扩容了，这里要重新获取
                                if ((rs = cells) != null
                                        && (m = rs.length) > 0
                                        && rs[j = (m - 1) & h] == null) {
                                    rs[j] = r;    // 把上面新建的Cell放在cells的j位置处
                                    created = true; // 创建成功
                                }
                            } finally {
                                cellsBusy = 0;  // 相当于释放锁
                            }
                            if (created) {   // 创建成功了就返回，值已经放在新建的Cell里面了
                                break;
                            }
                            continue;           // Slot is now non-empty
                        }
                    }
                    collide = false;     // 标记当前未出现冲突
                } else if (!wasUncontended)       //wasUncontended表示前一次cas更新cell单元是否成功
                    wasUncontended = true;      //重新置为true，后面会重新计算线程的hash值
                else if (a.cas(v = a.value, ((fn == null) ? v + x : fn.applyAsLong(v, x))))  //当前线程对应的cell已经初始化，尝试cas修改，如果成功则直接break
                    break;
                else if (n >= NCPU || cells != as)  //如果cells数组的长度达到了CPU核心数永远不会再扩容了， 设置collide为false并通过下面的语句修改线程的probe再重新尝试
                    collide = false;            //扩容标识，置为false表示不会再进行扩容
                else if (!collide)
                    collide = true;
                else if (cellsBusy == 0 && casCellsBusy()) {    // 明确出现冲突了，尝试占有锁（也就是标记cellsBusy=1），并扩容
                    try {
                        if (cells == as) {      // Expand table unless stale
                            Cell[] rs = new Cell[n << 1];   //2倍扩容
                            for (int i = 0; i < n; ++i)
                                rs[i] = as[i];  //把旧数组元素拷贝到新数组中
                            cells = rs; // 重新赋值cells为新数组
                        }
                    } finally {
                        cellsBusy = 0;   // 释放锁
                    }
                    collide = false;    // 已解决冲突
                    continue;        // 使用扩容后的新数组重新尝试
                }
                h = advanceProbe(h);     // 更新失败或者达到了CPU核心数，重新生成probe，并重试

            //case2：cells没有加锁切没有被初始化，则尝试对它加锁，并初始化cells数组
            } else if (cellsBusy == 0   //如果cells没有正在进行创建或扩容cells
                            && cells == as
                            && casCellsBusy()) {    //尝试占有锁，如果返回成功，表示cellsBusy被修改为1，准备创建cells
                boolean init = false;    // 是否初始化成功
                try {
                    if (cells == as) {   // 检测是否有其它线程初始化过
                        Cell[] rs = new Cell[2];
                        rs[h & 1] = new Cell(x);   //根据当前线程的hash值计算映射的索引，并创建对应的Cell对象，Cell单元中的初始值x就是本次要累加的值
                        cells = rs;  // 赋值给cells数组
                        init = true;    // 初始化成功
                    }
                } finally {
                    cellsBusy = 0;   // 释放锁
                }
                if (init) { // 初始化成功直接返回，因为增加的值已经同时创建到Cell中了
                    break;
                }

            //如果在初始化过程中，另一个线程ThreadB也进入了longAccumulate方法，就会进入分支
            //case3：cells正在进行初始化，则尝试直接在base上进行累加操作
            } else if (casBase(v = base, ((fn == null) ? v + x : fn.applyAsLong(v, x)))) {   // 如果有其它线程在初始化cells数组中，就尝试更新base，如果成功了就返回
                break;
            }
        }
    }

    static final int getProbe() {   //直接去内存中获取当前线程的threadLocalRandomProbe
        return UNSAFE.getInt(Thread.currentThread(), PROBE);
    }


   */


}
