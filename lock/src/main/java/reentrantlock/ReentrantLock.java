package reentrantlock;

import aqs.AQS;

import java.util.concurrent.TimeUnit;
import java.util.Collection;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * ReentrantLock可重入且独占式锁，又分为公平锁FairSync和非公平锁NonfairSync
 */
public class ReentrantLock {

/*    private final Sync sync;

    abstract static class Sync extends AQS {

        abstract void lock();

        //获取非公平锁
        final boolean nonfairTryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState(); //获取同步状态state
            if (c == 0) {//state=0代表没有线程获取到锁
                if (compareAndSetState(0, acquires)) {  // 尝试修改同步状态state
                    // 同步状态修改成功，获取到锁
                    setExclusiveOwnerThread(current);    //设置当前获取到锁的线程是当前线程
                    //当前锁获取到了锁，state=acquires
                    return true;
                }
            }
            // 同步状态不为0，表示已经有线程获取了锁，判断获取锁的线程是否为当前线程
            else if (current == getExclusiveOwnerThread()) {
                // 获取锁的线程是当前线程
                int nextc = c + acquires;
                if (nextc < 0) {
                    throw new Error("Maximum lock count exceeded");
                }
                setState(nextc);
                //当前锁获取到了锁，state+=acquires
                return true;
            }
            // 获取锁的线程不是当前线程
            return false;
        }

        //尝试释放锁
        @Override
        protected final boolean tryRelease(int releases) {
            int c = getState() - releases;
            if (Thread.currentThread() != getExclusiveOwnerThread()) { //如果获取到锁的不是当前线程，抛异常
                throw new IllegalMonitorStateException();
            }
            boolean free = false;
            // 新的状态值是否为0，若为0，则表示该锁已经完全释放了，其他线程可以获取同步状态了
            if (c == 0) {
                free = true;
                setExclusiveOwnerThread(null);
            }
            setState(c);
            return free;
        }

        @Override
        protected final boolean isHeldExclusively() {
            return getExclusiveOwnerThread() == Thread.currentThread();
        }

        final ConditionObject newCondition() {
            return new ConditionObject();
        }

        final Thread getOwner() {
            return getState() == 0 ? null : getExclusiveOwnerThread();
        }

        final int getHoldCount() {
            return isHeldExclusively() ? getState() : 0;
        }

        final boolean isLocked() {
            return getState() != 0;
        }

        private void readObject(java.io.ObjectInputStream s)
                throws java.io.IOException, ClassNotFoundException {
            s.defaultReadObject();
            setState(0); // reset to unlocked state
        }
    }

    //非公平锁
    static final class NonfairSync extends Sync {
        @Override
        final void lock() {
            if (compareAndSetState(0, 1)) {     //尝试修改同步状态state，如果修改成功state=1，表示获取到了锁
                //AbstractOwnableSynchronizer的主要作用就是记录获取到独占锁的线程
                setExclusiveOwnerThread(Thread.currentThread());    //设置当前获取到锁的线程是当前线程
            } else {    //如果同步状态修改失败，则表示没有获取到锁
                acquire(1);
            }
        }

        //当前线程尝试获取锁   true获取锁成功
        @Override
        protected final boolean tryAcquire(int acquires) {
            return nonfairTryAcquire(acquires);
        }
    }

    static final class FairSync extends Sync {
        private static final long serialVersionUID = -3000897897090466540L;

        @Override
        final void lock() {
            acquire(1);
        }

        @Override
        protected final boolean tryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {
                if (!hasQueuedPredecessors() &&
                        compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            } else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0)
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            return false;
        }
    }

    public ReentrantLock() {
        sync = new NonfairSync();   //默认非公平锁
    }

    public ReentrantLock(boolean fair) {
        sync = fair ? new FairSync() : new NonfairSync();
    }

    @Override
    public void lock() {
        sync.lock();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.nonfairTryAcquire(1);
    }


    @Override
    public boolean tryLock(long timeout, TimeUnit unit)
            throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(timeout));
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }


    public int getHoldCount() {
        return sync.getHoldCount();
    }


    public boolean isHeldByCurrentThread() {
        return sync.isHeldExclusively();
    }


    public boolean isLocked() {
        return sync.isLocked();
    }


    public final boolean isFair() {
        return sync instanceof FairSync;
    }


    protected Thread getOwner() {
        return sync.getOwner();
    }

    public final boolean hasQueuedThreads() {
        return sync.hasQueuedThreads();
    }


    public final boolean hasQueuedThread(Thread thread) {
        return sync.isQueued(thread);
    }


    public final int getQueueLength() {
        return sync.getQueueLength();
    }


    protected Collection<Thread> getQueuedThreads() {
        return sync.getQueuedThreads();
    }


    public boolean hasWaiters(Condition condition) {
        if (condition == null)
            throw new NullPointerException();
        if (!(condition instanceof AbstractQueuedSynchronizer.ConditionObject))
            throw new IllegalArgumentException("not owner");
        return sync.hasWaiters((AbstractQueuedSynchronizer.ConditionObject) condition);
    }

    public int getWaitQueueLength(Condition condition) {
        if (condition == null)
            throw new NullPointerException();
        if (!(condition instanceof AbstractQueuedSynchronizer.ConditionObject))
            throw new IllegalArgumentException("not owner");
        return sync.getWaitQueueLength((AbstractQueuedSynchronizer.ConditionObject) condition);
    }

    protected Collection<Thread> getWaitingThreads(Condition condition) {
        if (condition == null)
            throw new NullPointerException();
        if (!(condition instanceof AbstractQueuedSynchronizer.ConditionObject))
            throw new IllegalArgumentException("not owner");
        return sync.getWaitingThreads((AbstractQueuedSynchronizer.ConditionObject) condition);
    }*/

}
