package leetcodecn.digui;

public class 整数反转7 {

    public static void main(String[] args) {
        System.out.println("7. 整数反转\n" +
                "给出一个 32 位的有符号整数，你需要将这个整数中每位上的数字进行反转。");
        System.out.println(reverse(-321));
        //   int x = Integer.parseInt("11111111111111111111111111111101", 2);
        //   System.out.println(x);
        System.out.println(Math.floor(Math.sqrt(4)));

        System.out.println(Integer.MAX_VALUE);
        System.out.println(Integer.MAX_VALUE - (Integer.MAX_VALUE / 10) * 10);
        System.out.println(Integer.MIN_VALUE - (Integer.MIN_VALUE / 10) * 10);
    }

    private int reverse3(int x) {
        if (x == 0) return x;
        int temp = Math.abs(x);
        long result = 0L;
        int yushu;
        while (temp > 0) {
            yushu = temp % 10;
            result = 10 * result + yushu;
            temp /= 10;
        }
        if (result > Integer.MAX_VALUE) return 0;
        return x > 0 ? (int) result : (int) -result;
    }

    private static int reverse(int x) {
        if(x == 0) return x;
        int temp = x;
        int result = 0, yushu;
        // System.out.println(Integer.MAX_VALUE - (Integer.MAX_VALUE / 10) * 10);
        // System.out.println(Integer.MIN_VALUE - (Integer.MIN_VALUE / 10) * 10);
        while(temp != 0) {
            yushu = temp % 10;
            System.out.println(yushu);
            if (result > Integer.MAX_VALUE / 10 || (result == Integer.MAX_VALUE / 10 && yushu > 7))
                return 0;
            if (result < Integer.MIN_VALUE / 10 || (result == Integer.MIN_VALUE / 10 && yushu < -8))
                return 0;
            result = 10 * result + yushu;
            temp /= 10;
        }
        return result;
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
