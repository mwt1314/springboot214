package proxy.dynamic;

//目标类
public class GunDog implements IDog{

    @Override
    public void run() {
        System.out.println("猎狗在跑");
    }
}