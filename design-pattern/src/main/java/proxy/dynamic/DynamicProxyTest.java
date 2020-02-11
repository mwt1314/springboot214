package proxy.dynamic;

public class DynamicProxyTest {

    public static void main(String[] args) {

        IDog dog = new GunDog();

        MyInvocationHandle handle = new MyInvocationHandle();
        handle.setTarget(dog);

        IDog proxy =(IDog) handle.newInstance();
        proxy.run();

    }

}
