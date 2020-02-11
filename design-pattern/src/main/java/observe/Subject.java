package observe;

import java.util.Observable;

public class Subject extends Observable {

    private int age;

    public void setAge(int age) {
        this.age = age;
        setChanged();   //修改标识，表明数据发生变化了
        notifyObservers();  //通知所有的监听者
    }

    public int getAge() {
        return age;
    }

}
