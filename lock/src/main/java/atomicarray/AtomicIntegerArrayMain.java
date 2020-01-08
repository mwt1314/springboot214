package atomicarray;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @author mawt
 * @description
 * @date 2020/1/8
 */
public class AtomicIntegerArrayMain {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        //原子数组并不是说可以让线程以原子方式一次性地操作数组中所有元素的数组。
        //而是指对于数组中的每个元素，可以以原子方式进行操作
        //原子数组类型其实可以看成原子类型组成的数组
        AtomicIntegerArray aia = new AtomicIntegerArray(16);
        aia.getAndIncrement(0);  // 将第0个元素原子地增加1
        //等同于
        AtomicInteger[] ais = new AtomicInteger[16];
        ais[0] = new AtomicInteger(1);
        ais[0].getAndIncrement();  // 将第0个元素原子地增加1

        aia.set(0, 123); //原子设置index=0位置的值
        aia.set(1, 2); //原子设置index=1位置的值

        int i = aia.get(1); //获取index=1位置的值

        //cas更新index=0位置的元素的值为
        int newVal = aia.accumulateAndGet(3, 555, (oldValue, newValue) -> {
            return 11111;
        });

        System.out.println(aia.toString());

        Field base = aia.getClass().getDeclaredField("base");
        Field shift = aia.getClass().getDeclaredField("shift");
        Field array = aia.getClass().getDeclaredField("array");

        base.setAccessible(true);
        shift.setAccessible(true);
        array.setAccessible(true);

        Unsafe unsafe = getUnsafe();
        int scale = unsafe.arrayIndexScale(int[].class);
        System.out.println("scale=" + scale);

        System.out.println("base=" + base.get(aia));
        System.out.println("shift=" + shift.get(aia));
        int[] arr = (int[]) array.get(aia);
        System.out.println("array=" + Arrays.toString(arr));
    }

    private static Unsafe getUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
