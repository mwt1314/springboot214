package singleton1;

import cn.hutool.core.lang.Singleton;

/**
 * @author mawt
 * @description 懒汉式
 * @date 2020/1/7
 */
public class Singleton2 {

    //2.本类内部创建对象实例
    private static Singleton2 instance = null;

    /**
     * 1.构造方法私有化，外部不能new
     */
    private Singleton2() {

    }

    //3.提供一个公有的静态方法，返回实例对象
    public static Singleton2 getInstance() {
        if (instance == null) {
            instance = new Singleton2();
        }
        return instance;
    }

}
