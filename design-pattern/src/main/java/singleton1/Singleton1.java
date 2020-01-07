package singleton1;

import cn.hutool.core.lang.Singleton;

/**
 * @author mawt
 * @description 饿汉式：典型的空间换时间，当类装载的时候就会创建类实例，不管你用不用，先创建出来，然后每次调用的时候，就不需要判断了，节省了运行时间
 * @date 2020/1/7
 */
public class Singleton1 {

    private static Singleton1 instance = new Singleton1();

    // 私有化构造方法
    private Singleton1() {

    }

    public static Singleton1 getInstance() {
        return instance;
    }

}
