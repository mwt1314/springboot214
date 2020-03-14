package leetcodecn.digui;

public class 整数反转7 {

    public static void main(String[] args) {
        System.out.println(reverse(1534236469));
    }

    public static int reverse(int x) {
        int rev = 0;
        while (x != 0) {
            int pop = x % 10;
            x /= 10;
            if (rev > Integer.MAX_VALUE / 10 || (rev == Integer.MAX_VALUE / 10 && pop > 7)) return 0;
            if (rev < Integer.MIN_VALUE / 10 || (rev == Integer.MIN_VALUE / 10 && pop < -8)) return 0;
            rev = rev * 10 + pop;
        }
        return rev;
    }

    public int reverse2(int x) {
        int y = 0;
        // 最大的值与最小的值为：[−2^31, 2^31 − 1]， 即：[-2147483648, 2147483647]
        // 如果y = y * 10 + x % 10溢出，则 y>=214748364 ，
        // 当y=214748364时，输入的值只能为：1463847412，此时不溢出
        //即：y > 214748364 || y < -214748364 必定溢出
        while (x != 0) {
            if (y > 214748364 || y < -214748364) {
                return 0;
            }
            y = y * 10 + x % 10;
            x = x / 10;
        }
        return y;
    }


}
