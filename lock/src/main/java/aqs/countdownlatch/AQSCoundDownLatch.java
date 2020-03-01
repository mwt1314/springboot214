package aqs.countdownlatch;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author mawt
 * @description
 * @date 2020/1/13
 */

/**
 * CountDownLatch计数器闭锁是一个能阻塞主线程，让其他线程满足特定条件下主线程再继续执行的线程同步工具
 * <p>
 * CountDownLatch 与 join 方法的区别，
 * 一个区别是调用一个子线程的 join（）方法后，该线程会一直被阻塞直到该线程运行完毕，而 CountDownLatch 则使用计数器允许子线程运行完毕或者运行中时候递减计数，也就是 CountDownLatch 可以在子线程运行任何时候让 await 方法返回而不一定必须等到线程结束；
 * 另外使用线程池来管理线程时候一般都是直接添加 Runable 到线程池这时候就没有办法在调用线程的 join 方法了，countDownLatch 相比 Join 方法让我们对线程同步有更灵活的控制
 * <p>
 * CountDownLatch 的应用场景：
 * 确保某个计算在其需要的所有资源都被初始化之后才执行
 * 确保某个服务在其依赖的所有其他服务都已经启动之后才启动
 * 等待直到每个操作的所有参与者都就绪再执行（比如打麻将时需要等待四个玩家就绪）
 */
public class AQSCoundDownLatch {

    private volatile int state;

    /*等待队列的队首结点(懒加载，这里体现为竞争失败的情况下，加入同步队列的线程执行到enq方法的时候会创
        建一个Head结点)。该结点只能被setHead方法修改。并且结点的waitStatus不能为CANCELLED*/
    //仅仅代表头结点，里面没有存放线程引用
    private transient volatile Node head;

    /**
     * 等待队列的尾节点，也是懒加载的。（enq方法）。只在加入新的阻塞结点的情况下修改
     */
    private transient volatile Node tail;

    public AQSCoundDownLatch(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count < 0");
        }
        this.state = count;
    }

    //Node结点是对每一个等待获取资源的线程的封装，其包含了需要同步的线程本身及其等待状态
    static final class Node {

        //标记线程是因为获取共享资源失败被阻塞添加到队列中的
        static final Node SHARED = new Node(); //共享模式

        //表示线程因为获取独占资源失败被阻塞添加到队列中的
        static final Node EXCLUSIVE = null; //独占模式

        //表示当前结点已取消调度。当timeout或被中断（响应中断的情况下），会触发变更为此状态，进入该状态后的结点将不会再变化
        //表示该线程因为被中断或者等待超时，需要从等待队列中取消等待
        static final int CANCELLED = 1;
        //被标识为该等待唤醒状态的后继结点，当其前继结点的线程释放了同步锁或被取消，将会通知该后继结点的线程执行。说白了，就是处于唤醒状态，只要前继结点释放锁，就会通知标识为SIGNAL状态的后继结点的线程执行
        static final int SIGNAL = -1;
        //与Condition相关，该标识的结点处于等待队列中，结点的线程等待在Condition上，当其他线程调用了Condition的signal()方法后，CONDITION状态的结点将从等待队列转移到同步队列中，等待获取同步锁
        static final int CONDITION = -2;
        //与共享模式相关，在共享模式中，该状态标识结点的线程处于可运行状态
        static final int PROPAGATE = -3;

        //表示当前Node结点的等待状态
        //共有5种取值CANCELLED、SIGNAL、CONDITION、PROPAGATE、0初始化状态
        //负值表示结点处于有效等待状态，而正值表示结点已被取消。所以源码中很多地方用>0、<0来判断结点的状态是否正常
        volatile int waitStatus;

        volatile Node prev;

        volatile Node next;

        volatile Thread thread;

        Node nextWaiter;

        final boolean isShared() {
            return nextWaiter == SHARED;
        }

        final Node predecessor() throws NullPointerException {
            Node p = prev;
            if (p == null) {
                throw new NullPointerException();
            } else {
                return p;
            }
        }

        Node() {
        }

        Node(Thread thread, Node mode) {
            this.nextWaiter = mode;
            this.thread = thread;
        }

        Node(Thread thread, int waitStatus) {
            this.waitStatus = waitStatus;
            this.thread = thread;
        }
    }

    //当前线程调用了该方法后，会递减计数器的值，递减后如果计数器为 0 则会唤醒所有调用await方法而被阻塞的线程，否则什么都不做
    public void countDown() {
        releaseShared(1);
    }

    //当前线程调用了CountDownLatch对象的await方法后，当前线程会被阻塞，直到下面的情况之一才会返回：
    // （1）当所有线程都调用了CountDownLatch对象的countDown方法后，也就是说计时器值为 0 的时候。
    // （2）其他线程调用了当前线程的interrupt（）方法中断了当前线程，当前线程会抛出InterruptedException异常后返回
    public void await() throws InterruptedException {
        acquireSharedInterruptibly(1);
    }

    //当线程调用了 CountDownLatch 对象的该方法后，当前线程会被阻塞，直到下面的情况之一发生才会返回：
    // （1）当所有线程都调用了 CountDownLatch 对象的 countDown 方法后，也就是计时器值为 0 的时候，这时候返回 true；
    //  (2) 设置的 timeout 时间到了，因为超时而返回 false；
    // （3）其它线程调用了当前线程的 interrupt（）方法中断了当前线程，当前线程会抛出 InterruptedException 异常后返回
    public boolean await(long timeout, TimeUnit unit)
            throws InterruptedException {
        return tryAcquireSharedNanos(1, unit.toNanos(timeout));
    }

    public static void main(String[] args) {
        AQSCoundDownLatch aqsCoundDownLatch = new AQSCoundDownLatch(10);
        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                try {
                    //https://segmentfault.com/a/1190000015807573
                } finally {
                    aqsCoundDownLatch.countDown();
                }
            });
            threadList.add(thread);
            thread.start();
        }
        try {
            //是可以响应中断的：其他线程调用了当前线程的interrupt（）方法中断了当前线程，当前线程会抛出InterruptedException异常后返回
            aqsCoundDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("执行完成");
    }

    private static void printState(List<Thread> threadList) {
        new Thread(() -> {
            for (Thread thread : threadList) {

            }
        }).start();
    }

    public final boolean tryAcquireSharedNanos(int arg, long nanosTimeout)
            throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        return tryAcquireShared(arg) >= 0 ||
                doAcquireSharedNanos(arg, nanosTimeout);
    }

    public final boolean releaseShared(int arg) {
        if (tryReleaseShared(arg)) {    //尝试释放共享锁，如果state=0代表成功，准备唤醒线程了
            //此时state由1变成0了，释放共享锁了
            //此时一定是单线程
            System.out.println("最后一个线程：" + Thread.currentThread().getName());

            doReleaseShared();
            return true;
        }
        return false;
    }

    //尝试释放锁
    protected boolean tryReleaseShared(int releases) {
        // Decrement count; signal when transition to zero
        for (; ; ) {
            int c = getState(); //获取state
            //等于0了，无法再释放了
            //防止计数器值为 0 后，其他线程又调用了countDown方法，状态值就会变成负数
            if (c == 0) {
                return false;
            }
            int nextc = c - 1;
            //尝试cas置换state为state-1
            if (compareAndSetState(c, nextc)) {
                //cas成功
                // 减为0的时候返回true，这时会唤醒后面排队的线程
                return nextc == 0;
            }
        }
    }

    private void doReleaseShared() {
        //遵循FIFO的规则唤醒线程
        for (; ; ) {
            Node h = head;
            if (h != null
                    && h != tail) { //不止有head节点
                int ws = h.waitStatus;
                if (ws == Node.SIGNAL) { //如果当前节点的ws状态为-1
                    if (!compareAndSetWaitStatus(h, Node.SIGNAL, 0)) {  //尝试修改当前节点的ws状态-1变为0，如果修改失败，continue
                        continue;            // loop to recheck cases
                    }
                    //尝试修改当前节点的ws状态-1变为0，成功
                    //唤醒当前节点
                    unparkSuccessor(h);
                } else if (ws == 0 &&
                        !compareAndSetWaitStatus(h, 0, Node.PROPAGATE)) {
                    continue;                // loop on failed CAS
                }
            }
            if (h == head) {                // loop if head changed

                break;
            }
        }
    }

    private void unparkSuccessor(Node node) {
        int ws = node.waitStatus;
        if (ws < 0) {
            compareAndSetWaitStatus(node, ws, 0);
        }
        Node s = node.next;
        if (s == null || s.waitStatus > 0) {
            s = null;
            for (Node t = tail; t != null && t != node; t = t.prev) {
                if (t.waitStatus <= 0) {
                    s = t;
                }
            }
        }
        if (s != null) {
            //唤醒线程
            LockSupport.unpark(s.thread);
        }
    }

    protected final int getState() {
        return state;
    }

    protected final boolean compareAndSetState(int expect, int update) {
        // See below for intrinsics setup to support this
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }

    public final void acquireSharedInterruptibly(int arg)
            throws InterruptedException {
        //        //清楚中断标识，并返回线程中断状态
        if (Thread.interrupted()) { //响应中断
            throw new InterruptedException();
        }

        //等待其它线程完成的方法，它会先尝试获取一下共享锁，
        // 如果成功，则state=0
        // 如果失败，则进入AQS的队列中排队等待被唤醒
        //state不等于0的时候tryAcquireShared()返回的是-1，也就是说count未减到0的时候所有调用await()方法的线程都要排队
        if (tryAcquireShared(arg) < 0) {    //如果state=0返回1 否则返回-1
            //此时获取共享锁失败，需要进入等待队列，等待被唤醒
            doAcquireSharedInterruptibly(arg);  //可以响应中断
        }
    }

    //尝试获取共享锁
    protected int tryAcquireShared(int acquires) {
        //注意：这里state=0的时候总是返回1，也就是说state减为0的时候获取总是成功
        //state不等于0的时候返回-1，也就是state不等于0的时候总是要排队
        return (getState() == 0) ? 1 : -1;
    }

    private void doAcquireSharedInterruptibly(int arg) throws InterruptedException {
        final Node node = addWaiter(Node.SHARED);   //当前节点以共享的方式加入到链表尾部
        boolean failed = true;  //获取共享锁失败了
        try {
            for (; ; ) {
                final Node p = node.predecessor(); //p是node的前置节点
                if (p == head) {
                    int r = tryAcquireShared(arg);  //尝试获取共享锁state，如果state=0获取成功返回1，否则获取失败返回-1
                    if (r >= 0) {
                        //此时，获取共享锁state=0成功，准备唤醒线程了
                        setHeadAndPropagate(node, r);
                        p.next = null; // help GC
                        failed = false;
                        return;
                    }
                }

                if (shouldParkAfterFailedAcquire(p, node) &&    //
                        parkAndCheckInterrupt()) {  //使当前线程进入等待状态，并返回当前线程的中断标识
                    //当前线程被中断了，直接抛出异常
                    throw new InterruptedException();
                }
            }
        } finally {
            if (failed) {   //如果失败或出现异常，失败 取消该节点，以便唤醒后续节点
                cancelAcquire(node);
            }
        }
    }

    private void cancelAcquire(Node node) {
        // Ignore if node doesn't exist
        if (node == null)
            return;

        node.thread = null;

        // Skip cancelled predecessors
        Node pred = node.prev;
        while (pred.waitStatus > 0) {
            node.prev = pred = pred.prev;
        }

        // predNext is the apparent node to unsplice. CASes below will
        // fail if not, in which case, we lost race vs another cancel
        // or signal, so no further action is necessary.
        Node predNext = pred.next;

        // Can use unconditional write instead of CAS here.
        // After this atomic step, other Nodes can skip past us.
        // Before, we are free of interference from other threads.
        node.waitStatus = Node.CANCELLED;

        // If we are the tail, remove ourselves.
        if (node == tail && compareAndSetTail(node, pred)) {
            compareAndSetNext(pred, predNext, null);
        } else {
            // If successor needs signal, try to set pred's next-link
            // so it will get one. Otherwise wake it up to propagate.
            int ws;
            if (pred != head &&
                    ((ws = pred.waitStatus) == Node.SIGNAL ||
                            (ws <= 0 && compareAndSetWaitStatus(pred, ws, Node.SIGNAL))) &&
                    pred.thread != null) {
                Node next = node.next;
                if (next != null && next.waitStatus <= 0) {
                    compareAndSetNext(pred, predNext, next);
                }
            } else {
                unparkSuccessor(node);
            }

            node.next = node; // help GC
        }
    }

    private final boolean parkAndCheckInterrupt() {
        LockSupport.park(this);
        return Thread.interrupted();
    }

    private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
        int ws = pred.waitStatus;   //获取前驱节点的状态
        if (ws == Node.SIGNAL) {    //如果前驱节点的状态为signal
            return true;
        }
        if (ws > 0) {   //如果前节点状态大于0表明已经中断，就是CANCELLED状态
            do {
                node.prev = pred = pred.prev;
            } while (pred.waitStatus > 0);  //移除当前节点前面所有CANCELLED状态的节点
            pred.next = node;
        } else {
            compareAndSetWaitStatus(pred, ws, Node.SIGNAL); //尝试修改前驱结点的waitStatus=SIGNAL
        }
        return false;   //只有前驱节点状态为SIGNAL才返回真
    }

    private void setHeadAndPropagate(Node node, int propagate) {
        Node h = head; // Record old head for check below
        setHead(node);
        if (propagate > 0 || h == null || h.waitStatus < 0 ||
                (h = head) == null || h.waitStatus < 0) {
            Node s = node.next;
            if (s == null || s.isShared()) {
                doReleaseShared();
            }
        }
    }

    private boolean doAcquireSharedNanos(int arg, long nanosTimeout)
            throws InterruptedException {
        if (nanosTimeout <= 0L) {
            return false;
        }
        final long deadline = System.nanoTime() + nanosTimeout;
        final Node node = addWaiter(Node.SHARED);
        boolean failed = true;
        try {
            for (; ; ) {
                final Node p = node.predecessor();
                if (p == head) {
                    int r = tryAcquireShared(arg);
                    if (r >= 0) {
                        setHeadAndPropagate(node, r);
                        p.next = null; // help GC
                        failed = false;
                        return true;
                    }
                }
                nanosTimeout = deadline - System.nanoTime();
                if (nanosTimeout <= 0L) {
                    return false;
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                        nanosTimeout > spinForTimeoutThreshold) {
                    LockSupport.parkNanos(this, nanosTimeout);
                }
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
            }
        } finally {
            if (failed) {
                cancelAcquire(node);
            }
        }
    }


    private void setHead(Node node) {
        head = node;
        node.thread = null;
        node.prev = null;
    }

    private Node addWaiter(Node mode) {
        Node node = new Node(Thread.currentThread(), mode); //首先new一个节点，该节点维护一个线程引用
        Node pred = tail;
        if (pred != null) {
            node.prev = pred;
            if (compareAndSetTail(pred, node)) {
                pred.next = node;
                return node;
            }
        }
        enq(node);   //设置失败，表明是第一个创建节点，或者是已经被别的线程修改过了会进入这里
        return node;
    }

    private Node enq(final Node node) {
        for (; ; ) {
            Node t = tail;
            if (t == null) { // Must initialize
                if (compareAndSetHead(new Node())) {
                    tail = head;     //初始化时 头尾节点相等
                }
            } else {
                node.prev = t;
                if (compareAndSetTail(t, node)) {
                    t.next = node;
                    return t;
                }
            }
        }
    }

    private static Unsafe unsafe = null;

    static {
        try {
            unsafe = getUnsafe();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static final long stateOffset;
    private static final long headOffset;
    private static final long tailOffset;
    private static final long waitStatusOffset;
    private static final long nextOffset;

    static final long spinForTimeoutThreshold = 1000L;

    static {
        try {
            stateOffset = unsafe.objectFieldOffset(AQSCoundDownLatch.class.getDeclaredField("state"));
            headOffset = unsafe.objectFieldOffset(AQSCoundDownLatch.class.getDeclaredField("head"));
            tailOffset = unsafe.objectFieldOffset(AQSCoundDownLatch.class.getDeclaredField("tail"));
            waitStatusOffset = unsafe.objectFieldOffset(Node.class.getDeclaredField("waitStatus"));
            nextOffset = unsafe.objectFieldOffset(Node.class.getDeclaredField("next"));

        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    private static Unsafe getUnsafe() throws NoSuchFieldException, IllegalAccessException {
        Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafeField.setAccessible(true);
        return (Unsafe) theUnsafeField.get(null);
    }

    private static final boolean compareAndSetWaitStatus(Node node, int expect, int update) {
        return unsafe.compareAndSwapInt(node, waitStatusOffset, expect, update);
    }

    private final boolean compareAndSetTail(Node expect, Node update) {
        return unsafe.compareAndSwapObject(this, tailOffset, expect, update);
    }

    private final boolean compareAndSetHead(Node update) {
        return unsafe.compareAndSwapObject(this, headOffset, null, update);
    }

    private static final boolean compareAndSetNext(Node node, Node expect, Node update) {
        return unsafe.compareAndSwapObject(node, nextOffset, expect, update);
    }

}
