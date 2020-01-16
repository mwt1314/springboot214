package clh;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author mawt
 * @description
 * @date 2020/1/16
 */
public class CLHLock implements Lock {

    AtomicReference<QNode> tail;

    ThreadLocal<QNode> myPred;

    ThreadLocal<QNode> myNode;

    public CLHLock() {
        tail = new AtomicReference<QNode>(new QNode(false)); //创建的默认队尾结点，默认是释放锁的状态（locked为false）
        myNode = new ThreadLocal<QNode>() {
            @Override
            protected QNode initialValue() {
                return new QNode();
            }
        };
        myPred = new ThreadLocal<QNode>() {
            @Override
            protected QNode initialValue() {
                return null;
            }
        };
    }


    @Override
    public void lock() {
        QNode qnode = myNode.get(); //当前线程创建一个节点QNode
        qnode.locked = Boolean.TRUE.booleanValue(); //当前线程需要获得锁
        QNode pred = tail.getAndSet(qnode); //取到前趋节点QNode pred，然后设置当前线程的节点到队尾
        myPred.set(pred); //设置当前线程的前趋节点QNode pred
        while (pred.locked) {
        } //一直循环直到前趋节点pred释放锁（pred.locked为false）
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        QNode qnode = myNode.get(); //当前线程锁的QNode节点
        qnode.locked = Boolean.FALSE.booleanValue(); //设置当前线程释放锁的状态
        //myNode.set(myPred.get());
        myNode.remove(); //解除myNode绑定在当前线程上，即使下次当前线程再次需要获得锁时则再创建一个QNode。
        myPred.remove();
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    private static class QNode {
        final int id;

        volatile boolean locked;

        final String name = Thread.currentThread().getName() + "-QNode";

        private final static AtomicInteger idCount = new AtomicInteger(0);

        QNode() {
            this.id = idCount.getAndIncrement();
        }

        QNode(boolean locked) {
            this();
            this.locked = locked;
        }

        @Override
        public String toString() {
            return "QNode{id:" + id + ", locked:" + locked + ", name:" + name + "}";
        }

    }
}
