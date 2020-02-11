package observe;

public class Test {

    public static void main(String[] args) {
        Subject subject = new Subject();    //被监听者

        ObserverImpl observer1 = new ObserverImpl();    //监听者1
        ObserverImpl2 observer2 = new ObserverImpl2();    //监听者2

        //添加观察者对象
        subject.addObserver(observer1);
        subject.addObserver(observer2);

        System.out.println("old age is:" + subject.getAge());
        subject.setAge(1);  //数据更新了

    }

}
