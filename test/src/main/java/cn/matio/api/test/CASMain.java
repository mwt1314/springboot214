package cn.matio.api.test;

/**
 * @author mawt
 * @description
 * @date 2019/12/27
 */
public class CASMain {

    private int value;

    // 获取内存值
    private synchronized int getMemoryValue() {
        return value;
    }

    private synchronized int compareAndSwap(int expectValue, int newValue) {
        //获取旧值
        int oldValue = value;

        if (oldValue == expectValue) {
            this.value = newValue;
        }
        //返回修改之前的值
        return oldValue;
    }

    // 判断是否设置成功
    private synchronized boolean compareAndSet(int expectedValue, int newValue) {
        return expectedValue == compareAndSwap(expectedValue, newValue);
    }

    public static void main(String[] args) {
        CASMain cas = new CASMain();
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int expectedValue =  cas.getMemoryValue();
                    boolean b = cas.compareAndSet(expectedValue, (int)(Math.random()*100));
                    System.out.println(b);
                }
            }).start();
        }
    }

}
