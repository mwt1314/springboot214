package locksupoort;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author mawt
 * @description
 * @date 2020/1/7
 */
public class LockSupportMain {

    /**
     *
     * void park()
     *
     * 阻塞当前线程，如果掉用unpark(Thread)方法或被中断，才能从park()返回
     *
     * void parkNanos(long nanos)
     *
     * 阻塞当前线程，超时返回，阻塞时间最长不超过nanos纳秒
     *
     * void parkUntil(long deadline)
     *
     * 阻塞当前线程，直到deadline时间点
     *
     * void unpark(Thread)
     *
     * 唤醒处于阻塞状态的线程
     */

    //采用parkNanos(long nanos)阻塞线程
    public static void main(String[] args) {
        System.out.println(1);
        //当期线程进入TIMED_WAITING超时等待状态
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(200));
        System.out.println(12);
    }

}
