package lock1;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 上边是TicketLock的源码，是如何实现的自旋？当第一个线程获取Lock的时候，myticket是0，但是ticketNum已经变成了1，
 * 这个时候myticket和serviceNum的value是相等的，于是继续运行。
 * 此时，第二个线程获取的myticket是1，但是serviceNum的value还是0，就会在while上自旋，直到第一个线程准备调用unLock方法，
 * 把serviceNum的值修改为第一个线程的myticket加1。由于value是volatile的，所以第二个线程此时跳出了while循环。
 * 通过这种方式完成了自旋，避免了加锁导致的阻塞以及线程切换。
 *
 */
public class TicketLock1 {

    private AtomicInteger serviceNum = new AtomicInteger();

    private AtomicInteger ticketNum = new AtomicInteger();

    private static final ThreadLocal<Integer> LOCAL = new ThreadLocal<Integer>();

    public void lock() {
        int myticket = ticketNum.getAndIncrement();
        LOCAL.set(myticket);
        while (myticket != serviceNum.get()) {

        }
    }

    public void unlock() {
        int myticket = LOCAL.get();
        serviceNum.compareAndSet(myticket, myticket + 1);
    }

}
