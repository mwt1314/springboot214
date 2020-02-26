package state;

public class waitThis {

    public static void main(String[] args) {
        waitThis waitThis = new waitThis();
        new Thread(() -> {
            waitThis.self();
        }).start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        waitThis.notify();
    }

    private synchronized void self() {
        try {
            wait(); //如果方法上没有加synchronized关键字，编译通过，运行时报错了java.lang.IllegalMonitorStateException
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println(123);
        }
    }

}
