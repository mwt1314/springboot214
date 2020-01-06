package thread.state;

public class join {

    public static void main(String[] args) {
        Thread mainThread = Thread.currentThread();

        //当前线程等待该线程执行完成才会执行

        Thread subThread = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        subThread.start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println("subThread状态为：" + subThread.getState().name());  //TIMED_WAITING
                System.out.println("mainThread状态为：" + mainThread.getState().name());    //WAITING
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        try {
            subThread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("subThread已经运行完成");
    }

}
