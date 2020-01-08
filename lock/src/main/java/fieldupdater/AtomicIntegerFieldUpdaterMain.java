package fieldupdater;

import sun.misc.Unsafe;
import sun.reflect.Reflection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @author mawt
 * @description
 * @date 2020/1/8
 */
public class AtomicIntegerFieldUpdaterMain {

    //AtomicXXXFieldUpdater可以以一种线程安全的方式操作非线程安全对象的某些字段

    public static void main(String[] args) throws InterruptedException {
        //java.lang.InternalError: CallerSensitive annotation expected at frame 1
        //Class<?> clazz = Reflection.getCallerClass();
        Unsafe unsafe = null;

        Account account = new Account(0);

        List<Thread> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Thread t = new Thread(new Task(account));
            list.add(t);
            t.start();
        }

        for (Thread t : list) {
            t.join();
        }

        System.out.println(account.toString());
    }

    private static class Task implements Runnable {
        private Account account;

        Task(Account account) {
            this.account = account;
        }

        @Override
        public void run() {
            account.increMoney();
        }
    }

    static class Account {


        //必须是int类型
        //必须是volatile
        private volatile int money;

        private static final AtomicIntegerFieldUpdater<Account> updater = AtomicIntegerFieldUpdater.newUpdater(Account.class, "money");  // 引入AtomicIntegerFieldUpdater

        Account(int initial) {
            this.money = initial;
        }

        public void increMoney() {
            updater.incrementAndGet(this);    // 通过AtomicIntegerFieldUpdater操作字段
        }

        public int getMoney() {
            return money;
        }

        @Override
        public String toString() {
            return "Account{" +
                    "money=" + money +
                    '}';
        }
    }


}
