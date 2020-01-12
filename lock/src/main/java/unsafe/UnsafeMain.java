package unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author mawt
 * @description jdk1.8 Unsafe源码分析
 * @date 2020/1/8
 */
public class UnsafeMain {

    public static void main(String[] args) {
        Unsafe unsafe = getUnsafe();

        //返回指定数组的元素所占用的字节数
        //比如int[]数组中的每一个int元素占用4个字节
        assert unsafe != null;
        int intScale = unsafe.arrayIndexScale(int[].class);

        //返回指定类型数组的第一个元素地址相对于数组起始地址的偏移值
        int base = unsafe.arrayBaseOffset(int[].class);

        //公式：索引i的元素在数组中的内存起始地址
        //base + i * intScale

        System.out.println("intScale=" + intScale);
        System.out.println("doubleScale=" + unsafe.arrayIndexScale(double[].class));
        System.out.println("stringScale=" + unsafe.arrayIndexScale(String[].class));
        System.out.println("stringScale=" + unsafe.arrayIndexScale(String[].class));
        System.out.println("base=" + base);

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
