package unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.Arrays;

class SuperArray {
    private final static int BYTE = 1;
    private long size;
    private long address;

    public SuperArray(long size) {
        this.size = size;
        //得到分配内存的起始地址
        address = getUnsafe().allocateMemory(size * BYTE);
    }

    public void set(long i, byte value) {
        getUnsafe().putByte(address + i * BYTE, value);
    }

    private Unsafe getUnsafe() {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            return (Unsafe) theUnsafe.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int get(long idx) {
        return getUnsafe().getByte(address + idx * BYTE);
    }

    public long size() {
        return size;
    }

    public long address() {
        return address;
    }

    public static void main(String[] args) {
        //java中汉字如何存入内存
        byte[] bytes = "黑凤梨".getBytes();
        System.out.println(Arrays.toString(bytes));

        //分配指定长度内存，返回该段内存的首地址
        SuperArray sa = new SuperArray(bytes.length);
        System.out.println(sa.address);
        for (int i = 0; i < bytes.length; i++) {
            sa.set(i, bytes[i]);
        }

/*        sa.set(0L, (byte) 1);
        sa.set(1L, (byte) 2);
        sa.set(2L, (byte) 3);
        sa.set(3L, (byte) 4);
        sa.set(4L, (byte) 5);
        sa.set(5L, (byte) 6);
        sa.set(6L, (byte) 7);
        sa.set(7L, (byte) 8);*/
        int i = sa.get(0L);
        System.out.println(i);
    }

}

