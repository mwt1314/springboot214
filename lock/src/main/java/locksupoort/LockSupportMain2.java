package locksupoort;

import sun.misc.Unsafe;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author mawt
 * @description
 * @date 2020/1/7
 */
public class LockSupportMain2 {

    //采用parkNanos(Object blocker, long nanos)阻塞线程
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe) field.get(null);



        System.out.println(unsafe);


        System.out.println(2);
        //java.lang.Object@513259f9上的TIMED_WAITING
        //当期线程进入TIMED_WAITING超时等待状态
        LockSupport.parkNanos(new Object(), TimeUnit.SECONDS.toNanos(200));
        System.out.println(3);



    }

}
