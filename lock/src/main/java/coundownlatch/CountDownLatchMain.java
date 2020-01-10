package coundownlatch;

import java.util.concurrent.CountDownLatch;

/**
 * @author mawt
 * @description
 * @date 2020/1/10
 */
public class CountDownLatchMain {


    public static void main(String[] args) {
        int state = 10;

        //初始化state
        CountDownLatch countDownLatch = new CountDownLatch(state);
        for (int i = 0; i < state; i++) {
            new Thread(() -> {
                try {

                } finally {
                    countDownLatch.countDown();
                }
            }).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("执行完成");
    }
    /**
     countDownLatch.countDown();

     public void countDown() {
        sync.releaseShared(1);
     }

     public final boolean releaseShared(int arg) {
         if (tryReleaseShared(arg)) {   //尝试释放共享锁
            doReleaseShared();
            return true;
         }
         return false;
     }

     protected boolean tryReleaseShared(int releases) {
         for (;;) { //cas自旋
             int c = getState();
             if (c == 0)
                return false;
             int nextc = c-1;
             if (compareAndSetState(c, nextc))
                return nextc == 0;
         }
     }

    private void doReleaseShared() {
        for (;;) {
            Node h = head;
            if (h != null && h != tail) {
                int ws = h.waitStatus;
                if (ws == Node.SIGNAL) {
                    if (!compareAndSetWaitStatus(h, Node.SIGNAL, 0))
                        continue;            // loop to recheck cases
                    unparkSuccessor(h);
                }
                else if (ws == 0 &&
                        !compareAndSetWaitStatus(h, 0, Node.PROPAGATE))
                    continue;                // loop on failed CAS
            }
            if (h == head)                   // loop if head changed
                break;
        }
    }



    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    public final void acquireSharedInterruptibly(int arg) throws InterruptedException {
        if (Thread.interrupted())   //响应中断
            throw new InterruptedException();
        //如果state=0 if不执行了，所有在此countdownlatch上等待的线程没有阻塞
        //如果state<>0
        if (tryAcquireShared(arg) < 0)  //尝试获取共享锁  <0获取失败 =0获取成功 >0获取成功，且后继争用线程可能成功
            doAcquireSharedInterruptibly(arg);
    }

    protected int tryAcquireShared(int acquires) {
        return (getState() == 0) ? 1 : -1;  //当State==0时，表示无锁状态，且一旦State变为0，就永远处于无锁状态了，此时所有线程在await上等待的线程都可以继续执行
    }

    private void doAcquireSharedInterruptibly(int arg) throws InterruptedException {
        final Node node = addWaiter(Node.SHARED);   //包装成共享节点，插入等待队列
        boolean failed = true;
        try {
            for (;;) {  //自旋阻塞线程或尝试获取锁
                final Node p = node.predecessor();
                if (p == head) {
                    int r = tryAcquireShared(arg);  //尝试获取锁 如果state=0，返回1 否则返回-1
                    if (r >= 0) {   //获取锁成功
                        setHeadAndPropagate(node, r);
                        p.next = null; // help GC
                        failed = false;
                        return;
                    }
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                        parkAndCheckInterrupt())    //检查是否需要阻塞当前节点
                    throw new InterruptedException();
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }

     //表示当前结点已取消调度。当timeout或被中断（响应中断的情况下），会触发变更为此状态，进入该状态后的结点将不会再变化
     static final int CANCELLED = 1;

     //被标识为该等待唤醒状态的后继结点，当其前继结点的线程释放了同步锁或被取消，将会通知该后继结点的线程执行。说白了，就是处于唤醒状态，只要前继结点释放锁，就会通知标识为SIGNAL状态的后继结点的线程执行
     static final int SIGNAL = -1;

     //与Condition相关，该标识的结点处于等待队列中，结点的线程等待在Condition上，当其他线程调用了Condition的signal()方法后，CONDITION状态的结点将从等待队列转移到同步队列中，等待获取同步锁
     static final int CONDITION = -2;

     //与共享模式相关，在共享模式中，该状态标识结点的线程处于可运行状态
     static final int PROPAGATE = -3;

     private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) { //判断
        int ws = pred.waitStatus;
        if (ws == Node.SIGNAL) //如果前驱节点的waitStatus=-1，直接返回true；SIGNAL：后续节点需要被唤醒（这个状态说明当前节点的前驱节点将来会回来唤醒我，我可以安心地被阻塞）
            return true;
        if (ws > 0) {   //ws>0表示ws=1，CANCELLED状态：说明前驱节点因意外被中断/取消，需要将其从等待队列中删除
            do {
                node.prev = pred = pred.prev;
            } while (pred.waitStatus > 0);  //移除当前节点前面连续的CANCELLED状态的节点
            pred.next = node;
        } else {
            //对于独占功能来说，这里表示节点的初始状态0
            compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
        }
        return false;
    }
     */
}
