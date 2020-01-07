package locksupoort;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author mawt
 * @description
 * @date 2020/1/7
 */
public class UnSafeMain {

    static class Student {
        private int age = 5;

        public int getAge() {
            return age;
        }
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) theUnsafeField.get(null);

        Student student = new Student();
        System.out.println(student.getAge());
        Field field = student.getClass().getDeclaredField("age");
        unsafe.putInt(student, unsafe.objectFieldOffset(field), 10);
        System.out.println(student.getAge());

        int BYTE = 1;
        long address = unsafe.allocateMemory(BYTE);
        unsafe.putByte(address, (byte) 10);
        byte num = unsafe.getByte(address);
        System.out.println(num);
        unsafe.freeMemory(address);
    }

}
