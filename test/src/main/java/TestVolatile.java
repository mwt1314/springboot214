/**
 * @author mawt
 * @description
 * @date 2019/12/27
 */
// 使用 volatile 之前
public class TestVolatile {

    public static void main(String[] args) {
        //main线程
        ThreadDemo td = new ThreadDemo();
        new Thread(td).start();

        while (true) {
            if (td.isFlag()) {
                System.out.println("########");
                break;
            }
        }
    }
}

class ThreadDemo implements Runnable {
    private volatile boolean flag = false;

    @Override
    public void run() {

        //独立缓存
        try {
            // 该线程 sleep(2000), 导致了程序无法执行成功
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        flag = true;

        System.out.println("flag=" + isFlag());
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
