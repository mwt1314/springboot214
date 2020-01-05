package aqs;

import sun.misc.Unsafe;

import java.io.Serializable;
import java.util.concurrent.locks.LockSupport;

/**
 * AQS定义两种资源共享方式：
 * 1.exclusive独占，只有一个线程能执行，如ReentranLock
 * 2.share共享，多个线程可同时执行，如Semaphore和CountDownLatch
 */
public abstract class AQS extends BaseAQS implements Serializable {

    protected AQS() {

    }

    //Node结点是对每一个等待获取资源的线程的封装，其包含了需要同步的线程本身及其等待状态
    static final class Node {

        static final Node SHARED = new Node();

        static final Node EXCLUSIVE = null;

        //表示当前结点已取消调度。当timeout或被中断（响应中断的情况下），会触发变更为此状态，进入该状态后的结点将不会再变化
        static final int CANCELLED = 1;
        //表示后继结点在等待当前结点唤醒。后继结点入队时，会将前继结点的状态更新为SIGNAL
        static final int SIGNAL = -1;
        //表示结点等待在Condition上，当其他线程调用了Condition的signal()方法后，CONDITION状态的结点将从等待队列转移到同步队列中，等待获取同步锁
        static final int CONDITION = -2;
        //新结点入队时的默认状态
        static final int PROPAGATE = -3;

        //表示当前Node结点的等待状态
        //共有5种取值CANCELLED、SIGNAL、CONDITION、PROPAGATE、0新结点入队时的默认状态
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

    // head = new Node();
    private transient volatile Node head;

    private transient volatile Node tail;

    //当前同步状态
    private volatile int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    protected final boolean compareAndSetState(int expect, int update) {
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }


    static final long spinForTimeoutThreshold = 1000L;

    //用于将当前节点插入等待队列，如果队列为空，则初始化当前队列。
    // 整个过程以CAS自旋的方式进行，直到成功加入队尾为止
    private Node enq(final Node node) {
        for (; ; ) {
            Node t = tail;
            if (t == null) {
                //期望值null 新值new Node()
                if (compareAndSetHead(new Node())) {
                    tail = head;
                }
            } else {
                node.prev = t;
                //期望值t 新值node
                if (compareAndSetTail(t, node)) {
                    t.next = node;
                    return t;
                }
            }
        }
    }

    //该线程是否正在独占资源，只有用到condition才需要去实现它
    protected boolean isHeldExclusively() {
        throw new UnsupportedOperationException();
    }

    //独占方式。尝试以独占的方式获取资源，如果获取成功，则直接返回true，否则直接返回false
    protected boolean tryAcquire(int arg) {
        throw new UnsupportedOperationException();
    }

    //独占方式。尝试释放资源，成功则返回true，失败则返回false
    protected boolean tryRelease(int arg) {
        throw new UnsupportedOperationException();
    }

    //共享方式。尝试获取资源。负数表示失败；0表示成功，但没有剩余可用资源；正数表示成功，且有剩余资源
    protected int tryAcquireShared(int arg) {
        throw new UnsupportedOperationException();
    }

    //共享方式。尝试释放资源，如果释放后允许唤醒后续等待结点返回true，否则返回false
    protected boolean tryReleaseShared(int arg) {
        throw new UnsupportedOperationException();
    }

    // acquire是一种以独占方式获取资源，如果获取到资源，线程直接返回，否则进入等待队列，直到获取到资源为止，且整个过程忽略中断的影响。
    // 该方法是独占模式下线程获取共享资源的顶层入口。获取到资源后，线程就可以去执行其临界区代码了
    public final void acquire(int arg) {
        //tryAcquire：独占方式获取资源，成功为true
        //addWaiter：将该线程加入等待队列的尾部，并标记为独占模式
        //acquireQueued：使线程在等待队列中获取资源，一直获取到资源后才返回。如果在整个等待过程中被中断过，则返回true，否则返回false
        if (!tryAcquire(arg) && acquireQueued(addWaiter(Node.EXCLUSIVE), arg)) {
            selfInterrupt();
        }
    }

    //独占模式下线程释放共享资源的顶层入口
    //它会释放指定量的资源，如果彻底释放了（即state=0）,它会唤醒等待队列里的其他线程来获取资源
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

    static void selfInterrupt() {
        Thread.currentThread().interrupt();
    }

    //使线程在等待队列中获取资源，一直获取到资源后才返回。如果在整个等待过程中被中断过，则返回true，否则返回false

    //用于队列中的线程自旋地以独占且不可中断的方式获取同步状态（acquire），直到拿到锁之后再返回。该方法的实现分成两部分：
    // 如果当前节点已经成为头结点，尝试获取锁（tryAcquire）成功，然后返回；
    // 否则检查当前节点是否应该被park，然后将该线程park并且检查当前线程是否被可以被中断
    final boolean acquireQueued(final Node node, int arg) {
        //标记是否成功拿到资源，默认false
        boolean failed = true;
        try {
            //标记等待过程中是否被中断过
            boolean interrupted = false;
            //cas自旋
            for (; ; ) {
                //拿到前驱节点
                final Node p = node.predecessor();
                //如果前驱是head，即该结点已成老二，那么便有资格去尝试获取资源（可能是老大释放完资源唤醒自己的，当然也可能被interrupt了）。
                if (p == head && tryAcquire(arg)) {
                    //拿到资源后，将head指向该结点。所以head所指的标杆结点，就是当前获取到资源的那个结点或null。
                    setHead(node);
                    // setHead中node.prev已置为null，此处再将head.next置为null，就是为了方便GC回收以前的head结点。也就意味着之前拿完资源的结点出队了！
                    p.next = null; // help GC
                    // 成功获取资源
                    failed = false;
                    //返回等待过程中是否被中断过
                    return interrupted;
                }
                //如果自己可以休息了，就通过park()进入waiting状态，直到被unpark()。如果不可中断的情况下被中断了，那么会从park()中醒过来，发现拿不到资源，从而继续进入park()等待
                if (shouldParkAfterFailedAcquire(p, node) && parkAndCheckInterrupt()) {
                    interrupted = true;//如果等待过程中被中断过，哪怕只有那么一次，就将interrupted标记为true
                }
            }
        } finally {
            if (failed) { // 如果等待过程中没有成功获取资源（如timeout，或者可中断的情况下被中断了），那么取消结点在队列中的等待。
                cancelAcquire(node);
            }
        }
    }

    //通过对当前节点的前一个节点的状态进行判断，对当前节点做出不同的操作
    private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
        int ws = pred.waitStatus;//拿到前驱的状态
        if (ws == Node.SIGNAL) {
            //如果已经告诉前驱拿完号后通知自己一下，那就可以安心休息了
            return true;
        }
        if (ws > 0) {
            /*
             * 如果前驱放弃了，那就一直往前找，直到找到最近一个正常等待的状态，并排在它的后边。
             * 注意：那些放弃的结点，由于被自己“加塞”到它们前边，它们相当于形成一个无引用链，稍后就会被保安大叔赶走了(GC回收)！
             */
            do {
                node.prev = pred = pred.prev;
            } while (pred.waitStatus > 0);
            pred.next = node;
        } else {
            //如果前驱正常，那就把前驱的状态设置成SIGNAL，告诉它拿完号后通知自己一下。有可能失败，人家说不定刚刚释放完呢！
            compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
        }
        return false;
    }

    //让线程去休息，真正进入等待状态。park()会让当前线程进入waiting状态。
    // 在此状态下，有两种途径可以唤醒该线程：
    // 1）被unpark()；
    // 2）被interrupt()。
    // 需要注意的是，Thread.interrupted()会清除当前线程的中断标记位
    private final boolean parkAndCheckInterrupt() {
        LockSupport.park(this);//调用park()使线程进入waiting状态
        return Thread.interrupted();//如果被唤醒，查看自己是不是被中断的。
    }

    //该方法用于将当前线程根据不同的模式（Node.EXCLUSIVE互斥模式、Node.SHARED共享模式）加入到等待队列的队尾，并返回当前线程所在的结点。
    // 如果队列不为空，则以通过compareAndSetTail方法以CAS的方式将当前线程节点加入到等待队列的末尾。
    // 否则，通过enq(node)方法初始化一个等待队列，并返回当前节点
    private Node addWaiter(Node mode) {
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

    private void cancelAcquire(Node node) {
        // Ignore if node doesn't exist
        if (node == null) {
            return;
        }

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

    private void unparkSuccessor(Node node) {
        /*
         * If status is negative (i.e., possibly needing signal) try
         * to clear in anticipation of signalling.  It is OK if this
         * fails or if status is changed by waiting thread.
         */
        int ws = node.waitStatus;
        if (ws < 0) {
            compareAndSetWaitStatus(node, ws, 0);
        }

        /*
         * Thread to unpark is held in successor, which is normally
         * just the next node.  But if cancelled or apparently null,
         * traverse backwards from tail to find the actual
         * non-cancelled successor.
         */
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
            LockSupport.unpark(s.thread);
        }
    }

    private void setHead(Node node) {
        head = node;
        node.thread = null;
        node.prev = null;
    }

    private final boolean compareAndSetHead(Node update) {
        return unsafe.compareAndSwapObject(this, headOffset, null, update);
    }

    private final boolean compareAndSetTail(Node expect, Node update) {
        return unsafe.compareAndSwapObject(this, tailOffset, expect, update);
    }

    private static final boolean compareAndSetWaitStatus(Node node, int expect, int update) {
        return unsafe.compareAndSwapInt(node, waitStatusOffset, expect, update);
    }

    private static final boolean compareAndSetNext(Node node, Node expect, Node update) {
        return unsafe.compareAndSwapObject(node, nextOffset, expect, update);
    }

    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static long stateOffset;
    private static long headOffset;
    private static long tailOffset;
    private static long waitStatusOffset;
    private static long nextOffset;

    static {
        try {
            stateOffset = unsafe.objectFieldOffset(AQS.class.getDeclaredField("state"));
            headOffset = unsafe.objectFieldOffset(AQS.class.getDeclaredField("head"));
            tailOffset = unsafe.objectFieldOffset(AQS.class.getDeclaredField("tail"));
            waitStatusOffset = unsafe.objectFieldOffset(AQS.class.getDeclaredField("waitStatus"));
            nextOffset = unsafe.objectFieldOffset(AQS.class.getDeclaredField("next"));

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

}