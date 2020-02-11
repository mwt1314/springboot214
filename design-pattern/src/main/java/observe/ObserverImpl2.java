package observe;

import java.util.Observable;

public class ObserverImpl2 implements java.util.Observer {

    @Override
    public void update(Observable o, Object arg) {
        if (o != null && o instanceof Subject) {
            Subject subject = (Subject) o;
            System.out.println("2:new age is:" + subject.getAge());
        }
    }

}
