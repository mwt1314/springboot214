package cn.matio.api.test;

/**
 * @author mawt
 * @description
 * @date 2020/4/15
 */
public class BikeP {

    {
        System.out.println("父亲动态代码块1");
    }

    static {
        System.out.println("父亲静态代码块2");
    }

    BikeP() {
        System.out.println("parent new");

    }

    public static void main(String[] args) {
        Integer x = 128;
        Integer y = 128;
        System.out.println(x == y);
    }

}
