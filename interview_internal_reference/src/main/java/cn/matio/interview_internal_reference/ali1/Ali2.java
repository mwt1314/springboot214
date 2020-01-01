package cn.matio.interview_internal_reference.ali1;

/**
 * 01.阿里篇/1.1.2 已知sqrt(2)约等于1.414，要求不用数学库，求sqrt(2)精确到小数点后10位.md
 */
public class Ali2 {

    private static final double EPSILON = 0.0000000001;

    public static void main(String[] args) {
        System.out.println(sqrt2());
    }

    //二分法
    private static double sqrt2() {
        double low = 1.4, high = 1.5;
        double mid = (low + high) / 2;
        while (high - low > EPSILON) {
            if (mid * mid > 2) {
                high = mid;
            } else {
                low = mid;
            }
            mid = (high + low) / 2;
        }
        System.out.println(high);
        System.out.println(low);
        return mid;
    }

    private static double newton(double x) {
        if (Math.abs(x * x - 2) > EPSILON) {
            return newton(x - (x * x - 2) / (2 * x));
        } else
            return x;
    }

}
