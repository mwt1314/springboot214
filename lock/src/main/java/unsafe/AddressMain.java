package unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author mawt
 * @description
 * @date 2020/5/26
 */
public class AddressMain {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        //反射获取unsafe
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Unsafe unsafe = (Unsafe) f.get(null);

        String[] array1 = new String[]{"abc", "efg", "hij", "kl", "mn", "xyz"};
        String[] array2 = new String[]{"abc1", "efg1", "hij1", "kl1", "mn1", "xyz1"};
        Class<?> ak = String[].class;

        //返回指定类型数组的第一个元素相对于数组起始地址的偏移值
        long ABASE = unsafe.arrayBaseOffset(ak);

        //返回指定数组的元素所占用的字节数
        //比如int[]数组中的每一个int元素占用4个字节
        //可以获取数组的转换因子，也就是数组中元素的增量地址。将arrayBaseOffset与arrayIndexScale配合使用，可以定位数组中每个元素在内存中的位置
        int scale = unsafe.arrayIndexScale(ak);
        //例如获取array数组中下标为n的元素
        //地址总偏移为：ABASE + n * scale
        //            ABASE + n << ASHIFT
        //

        int ASHIFT = 31 - Integer.numberOfLeadingZeros(scale);
        String array11 = (String) unsafe.getObject(array1, ((long) 2 << ASHIFT) + ABASE);
        String array21 = (String) unsafe.getObject(array2, ((long) 2 << ASHIFT) + ABASE);
        System.out.println(ABASE);
        System.out.println(scale);
        System.out.println(ASHIFT);
        System.out.println(array11);
        System.out.println(array21);
    }


    // 首先在jvm中一个int类型的数据占4个字节，共32位，其实就相当于一个长度为32的数组。
    // 那我们要计算首部0的个数，就是从左边第一个位开始累加0的个数，直到遇到一个非零值。
    public static int numberOfLeadingZeros(int i) {
        if (i == 0)
            return 32;
        int n = 1;
        // 下面的代码就是定位从左边开始第一个非零值的位置，在定位过程中顺便累加从左边开始0的个数
        // 将i无符号右移16位后，有二种情况；
        //   情况1.i=0,则第一个非零值位于低16位，i至少有16个0，同时将i左移16位（把低16位移到原高16位的位置，这样情况1和情况2就能统一后续的判断方式）
        //   情况2.i!=0,则第一个非零值位于高16位，后续在高16位中继续判断
        // 这个思路就是二分查找，首先把32位的数分为高低16位，如果非零值位于高16位，后续再将高16位继续二分为高低8位，一直二分到集合中只有1个元素
        if (i >>> 16 == 0) { n += 16; i <<= 16; }
        // 判断第一个非零值是否位于高8位
        if (i >>> 24 == 0) { n +=  8; i <<=  8; }
        // 判断第一个非零值是否位于高4位
        if (i >>> 28 == 0) { n +=  4; i <<=  4; }
        // 判断第一个非零值是否位于高2位
        if (i >>> 30 == 0) { n +=  2; i <<=  2; }
        // 判断第一个非零值是否位于左边第一位
        n -= i >>> 31;
        return n;
    }

}
