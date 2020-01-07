package singleton1;

/**
 * @author mawt
 * @description 懒汉式：双重校验锁
 * @date 2020/1/7
 */
public class Singleton3 {

    //volatile：内存可见性+防止指令重排
    private static volatile Singleton3 instance = null;

    private Singleton3() {

    }

    public static Singleton3 getInstance() {
        if (instance == null) {
            synchronized (Singleton3.class) {
                if (instance == null) {
                    instance = new Singleton3();
                }
            }
        }
        return instance;
    }

}
