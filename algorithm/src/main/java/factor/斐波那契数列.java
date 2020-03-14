package factor;

public class 斐波那契数列 {

    private static final int N = 47;

    public static void main(String[] args) {
        System.out.println("已上传到CSDN");
        System.out.println("斐波那契数列");

        System.out.println("递归实现斐波那契数列");
        System.out.println(fibonacci(N));

        System.out.println("for循环实现斐波那契数列");
        System.out.println(fibonacci2(N));

        System.out.println("尾递归斐波那契数列");
        System.out.println(Fib(N));
    }

    //使用递归的方法计算斐波那契数列第n项
    private static long fibonacci(int n) {
        if (n == 1 || n == 2) {
            return 1;
        }
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    private static long Fib(int n) {
        if (n < 2) return n;
        return fibonacci3(n, 1, 1, 3);
    }

    //使用尾递归的方式计算斐波那契数列第n项
    //参数不建议使用int类型，long转int时会溢出
    private static long fibonacci3(long n, long r1, long r2, long begin) {
        if (n == begin) return r1 + r2;
        return fibonacci3(n, r2, r1 + r2, ++begin);
    }

    private static long fibonacci2(int n) {
        if (n == 1 || n == 2) {
            return 1;
        }
        long f1 = 1L, f2 = 1L;
        long fn = 0L;

        while (n > 2) {
            fn = f1 + f2;
            f1 = f2;
            f2 = fn;
            n--;
        }
        return fn;
    }

}
