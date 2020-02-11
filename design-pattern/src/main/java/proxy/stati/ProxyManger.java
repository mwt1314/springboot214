package proxy.stati;

// 代理类需要有真实对象的控制权 (引用)
public class ProxyManger implements IStar {

    // 真实对象的引用
    private IStar star;

    public ProxyManger() {
        super();
    }

    public ProxyManger(IStar star) {
        super();
        this.star = star;
    }

    @Override
    public void sing() {
        System.out.println("唱歌前准备");
        star.sing();
        System.out.println("善后工作");
    }

}