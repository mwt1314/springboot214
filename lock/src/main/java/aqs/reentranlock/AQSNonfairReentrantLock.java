package aqs.reentranlock;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.locks.LockSupport;

/**
 * @author mawt
 * @description ReentrantLock是一个可重入且独占式的锁，又分为非公平锁和公平锁
 * <p>
 * 非公平锁测试：https://www.cnblogs.com/fsmly/p/11274572.html
 * @date 2020/1/16
 */
public class AQSNonfairReentrantLock {

    private volatile int state;

    /*等待队列的队首结点(懒加载，这里体现为竞争失败的情况下，加入同步队列的线程执行到enq方法的时候会创
        建一个Head结点)。该结点只能被setHead方法修改。并且结点的waitStatus不能为CANCELLED*/
    //仅仅代表头结点，里面没有存放线程引用
    private transient volatile Node head;

    /**
     * 等待队列的尾节点，也是懒加载的。（enq方法）。只在加入新的阻塞结点的情况下修改
     */
    private transient volatile Node tail;

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

    final void lock() {
        //尝试获取锁
        if (compareAndSetState(0, 1)) {
            //缓存当前获取到锁的是哪一个线程，此时一定是单线程更新
            setExclusiveOwnerThread(Thread.currentThread());
        } else {
            //没有获取到锁，进入等待队列
            acquire(1);
        }
    }

    public void unlock() {
        release(1);
    }

    public static void main(String[] args) {
        //非公平锁
        AQSNonfairReentrantLock aqsNonfairReentrantLock = new AQSNonfairReentrantLock();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                try {
                    aqsNonfairReentrantLock.lock();

                } finally {
                    aqsNonfairReentrantLock.unlock();
                }

            });
            thread.start();
        }
    }

    public final boolean release(int arg) {
        if (tryRelease(arg)) {
            Node h = head;
            if (h != null && h.waitStatus != 0) {
                unparkSuccessor(h);
            }
            return true;
        }
        return false;
    }

    protected final boolean tryRelease(int releases) {
        int c = getState() - releases;
        //判断获取锁的是不是当期线程
        if (Thread.currentThread() != getExclusiveOwnerThread()) {
            throw new IllegalMonitorStateException();
        }
        boolean free = false;
        if (c == 0) {
            free = true;
            setExclusiveOwnerThread(null);
        }
        setState(c);
        return free;
    }

    public final void acquire(int arg) {
        if (!tryAcquire(arg) && //尝试获取锁，成功true，失败false
                acquireQueued(addWaiter(Node.EXCLUSIVE), arg)) {    //如果当前线程获取锁失败了，当前线程以独占的方式加入到等待队列队尾，等待被唤醒
            selfInterrupt();    //当断当期线程
        }
    }

    protected final boolean tryAcquire(int acquires) {
        return nonfairTryAcquire(acquires);
    }

    /**
     * 这个方法的主要作用就是在同步队列中嗅探到自己的前驱结点，如果前驱结点是头节点的话就会尝试取获取同步状态，
     * 否则会先设置自己的waitStatus为-1，然后调用LockSupport的方法park自己
     *
     * @param node 当前线程的包装节点，waitStatus初始化默认值为0
     * @param arg  固定值1
     * @return
     */
    final boolean acquireQueued(final Node node, int arg) { //该方法会被多线程访问
        //遵循FIFO的规则：从head开始依次获取锁
        //当期节点被加入到等待队列中后，
        //判断当期节点的前驱节点是不是head，如果是，说明处于等待队列中的第一位，需要被处理了
        //如果不是，设置当前节点的waitStatus=-1，标识当前节点需要被前驱节点唤醒
        boolean failed = true;  //获取锁标识
        try {
            boolean interrupted = false;
            for (; ; ) {    //自旋
                final Node p = node.predecessor();  //当前节点的前驱节点
                if (p == head //如果当前节点的前驱节点是head
                        && tryAcquire(arg)) {   //如果尝试获取锁成功
                    setHead(node);  //单线程更新？？？？？
                    p.next = null; // help GC
                    failed = false;
                    return interrupted;
                }
                //如果不是头节点,或者虽然前驱结点是头节点但是尝试获取同步状态失败就会将node结点
                //的waitStatus设置为-1(SIGNAL),并且park自己，等待前驱结点的唤醒
                if (shouldParkAfterFailedAcquire(p, node)   //如果获取锁失败，判断是否需要阻塞当期线程
                        && parkAndCheckInterrupt()) {   //阻塞当期线程，并返回当期线程的中断标识并清除中断标识
                    interrupted = true;
                }
            }
        } finally {
            if (failed) {   //获取锁失败了
                cancelAcquire(node);
            }
        }
    }

    private void cancelAcquire(Node node) {
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

    static void selfInterrupt() {
        Thread.currentThread().interrupt();
    }

    private final boolean parkAndCheckInterrupt() {
        LockSupport.park(this);
        return Thread.interrupted();
    }

    //如果前驱节点的ws=-1，返回true；其他的 返回false
    private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
        int ws = pred.waitStatus;   //获取前驱节点的状态
        if (ws == Node.SIGNAL) {    //如果前驱节点的状态为signal=-1
            //如果前驱节点的状态为-1，说明当前节点需要被其前驱节点唤醒，此时要阻塞当期节点，所以返回true
            return true;
        }
        if (ws > 0) {   //如果前驱节点状态大于0表明已经超时或者中断，此时要把他们移出等待队列了
            //这里就是将所有的前驱结点状态为CANCELLED的都移除
            do {
                node.prev = pred = pred.prev;
            } while (pred.waitStatus > 0);
            pred.next = node;
        } else {
            compareAndSetWaitStatus(pred, ws, Node.SIGNAL); //尝试设置前驱节点的状态为SIGHNAL=-1
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


    private void setHead(Node node) {
        head = node;
        node.thread = null;
        node.prev = null;
    }

    // 总结来说，即使在多线程情况下，enq方法还是能够保证每个线程结点会被安全的添加到同步队列中，
    // 因为enq通过CAS方式将结点添加到同步队列之后才会返回，否则就会不断尝试添加(这样实际上就是在并发情况下，把向同步队列添加Node变得串行化了)
    private Node addWaiter(Node mode) { //此时可能是多线程方式添加队尾节点，使用cas机制同步
        //(1)将当前线程以及阻塞原因(是因为SHARED模式获取state失败还是EXCLUSIVE获取失败)构造为Node结点
        Node node = new Node(Thread.currentThread(), mode);
        // Try the fast path of enq; backup to full enq on failure
        Node pred = tail;
        if (pred != null) {
            node.prev = pred;
            if (compareAndSetTail(pred, node)) {
                pred.next = node;
                return node;
            }
        }
        enq(node);
        return node;
    }

    private Node enq(final Node node) {
        for (; ; ) {
            Node t = tail;
            if (t == null) { // Must initialize
                if (compareAndSetHead(new Node())) { //初始化head=new Node() head节点并不是线程节点，只是方便此后的业务实现加上的
                    tail = head;
                }
            } else {
                node.prev = t;
                if (compareAndSetTail(t, node)) {   //cas更新tail节点为新节点
                    t.next = node;
                    return t;
                }
            }
        }
    }

    /**
     * 当前线程尝试获取锁，可重入
     *
     * @param acquires
     * @return 如果获取锁成功，返回true，反之返回false
     */
    final boolean nonfairTryAcquire(int acquires) {
        final Thread current = Thread.currentThread();
        int c = getState();

        //先判断是否有线程获取到锁，如果有则state=1，否则为0
        if (c == 0) {
            //没有线程获取到锁，尝试获取一下
            if (compareAndSetState(0, acquires)) {
                //当前线程获取锁成功，，就设置当前独占模式下同步状态的持有者为当前线程
                setExclusiveOwnerThread(current);
                return true;
            }
        } else if (current == getExclusiveOwnerThread()) {  //这里是可重入锁的逻辑
            //当前线程获取到了锁
            int nextc = c + acquires;
            if (nextc < 0) {// 这里是风险处理
                throw new Error("Maximum lock count exceeded");
            }
            setState(nextc);    //没有使用cas方式更新state的原因：此时一定是单线程操作
            return true;
        }
        return false; //当前线程获取锁失败时，返回false，成功返回true
    }

    protected final boolean compareAndSetState(int expect, int update) {
        // See below for intrinsics setup to support this
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }

    protected final void setState(int newState) {
        state = newState;
    }

    protected final int getState() {
        return state;
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
            stateOffset = unsafe.objectFieldOffset
                    (AQSNonfairReentrantLock.class.getDeclaredField("state"));
            headOffset = unsafe.objectFieldOffset
                    (AQSNonfairReentrantLock.class.getDeclaredField("head"));
            tailOffset = unsafe.objectFieldOffset
                    (AQSNonfairReentrantLock.class.getDeclaredField("tail"));
            waitStatusOffset = unsafe.objectFieldOffset
                    (Node.class.getDeclaredField("waitStatus"));
            nextOffset = unsafe.objectFieldOffset
                    (Node.class.getDeclaredField("next"));

        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    private static Unsafe getUnsafe() throws NoSuchFieldException, IllegalAccessException {
        Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafeField.setAccessible(true);
        return (Unsafe) theUnsafeField.get(null);
    }

    private static final boolean compareAndSetWaitStatus(Node node,
                                                         int expect,
                                                         int update) {
        return unsafe.compareAndSwapInt(node, waitStatusOffset,
                expect, update);
    }

    private final boolean compareAndSetTail(Node expect, Node update) {
        return unsafe.compareAndSwapObject(this, tailOffset, expect, update);
    }

    private final boolean compareAndSetHead(Node update) {
        return unsafe.compareAndSwapObject(this, headOffset, null, update);
    }

    private static final boolean compareAndSetNext(Node node,
                                                   Node expect,
                                                   Node update) {
        return unsafe.compareAndSwapObject(node, nextOffset, expect, update);
    }

}
