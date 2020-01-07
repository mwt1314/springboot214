package thread.detail;

/**
 * @author mawt
 * @description
 * @date 2020/1/7
 */
public class ThreadMain {

    public static void main(String[] args) {
        Thread t1 = new Thread();
        Thread t2 = new Thread();
        System.out.println(t1.getName());
        System.out.println(t2.getName());

    }

}
