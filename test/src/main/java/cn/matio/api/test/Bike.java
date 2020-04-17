package cn.matio.api.test;

public class Bike extends BikeP {

    public static Bike bike1 = new Bike();

    public static Bike bike2 = new Bike();

    Bike() {
        System.out.println("new");
    }

    {
        System.out.println("动态代码块1");
    }

    static {
        System.out.println("静态代码块2");
    }

    public static void main(String[] args) {
        Integer x = 128;
        Integer y = 128;
        System.out.println(x == y);

        System.out.println("3");
        new Bike();
        System.out.println("4");
    }

}