package arraylist;

/**
 * @author mawt
 * @description
 * @date 2020/4/18
 */
public class xx {

    public static void main(String[] args) {
        System.out.println(tableSizeFor(1));
        System.out.println(tableSizeFor(2));
        System.out.println(tableSizeFor(8));
        System.out.println(tableSizeFor(11));
        System.out.println(tableSizeFor(16));
    }

    static final int MAXIMUM_CAPACITY = 1 << 30;
    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

}
