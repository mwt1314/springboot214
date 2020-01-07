package locksupoort;

import sun.misc.Unsafe;

/**
 * @author mawt
 * @description
 * @date 2020/1/7
 */
public class UnsafeCAS {

    static class MyAutomicInteger {

        private volatile int value = 0;
        private Unsafe unsafe;
        private long offset;

        public MyAutomicInteger(Unsafe unsafe) throws Exception {
            this.unsafe = unsafe;
            this.offset = unsafe.objectFieldOffset(MyAutomicInteger.class.getDeclaredField("value"));
        }

        public int getAndIncrement() {
            int oldValue = value;

            for (; ; ) {
                if (unsafe.compareAndSwapInt(this, offset, oldValue, oldValue + 1)) {
                    break;
                }

                oldValue = value;
            }
            return oldValue;
        }

    }

    public static void main(String[] args) {


    }

}
