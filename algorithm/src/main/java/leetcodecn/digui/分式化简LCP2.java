package leetcodecn.digui;

public class 分式化简LCP2 {

    public static void main(String[] args) {
        System.out.println("LCP 2. 分式化简\n" +
                "有一个同学在学习分式。他需要将一个连分数化成最简分数，你能帮助他吗？\n" +
                "连分数是形如上图的分式。在本题中，所有系数都是大于等于0的整数。\n" +
                "输入的cont代表连分数的系数（cont[0]代表上图的a0，以此类推）。" +
                "返回一个长度为2的数组[n, m]，使得连分数的值等于n / m，且n, m最大公约数为1。\n" +

                "示例 1：\n" +
                "输入：cont = [3, 2, 0, 2]\n" +
                "输出：[13, 4]\n" +
                "解释：原连分数等价于3 + (1 / (2 + (1 / (0 + 1 / 2))))。注意[26, 8], [-13, -4]都不是正确答案。\n" +

                "示例 2：\n" +
                "输入：cont = [0, 0, 3]\n" +
                "输出：[3, 1]\n" +
                "解释：如果答案是整数，令分母为1即可。\n" +

                "限制：\n" +
                "cont[i] >= 0\n" +
                "1 <= cont的长度 <= 10\n" +
                "cont最后一个元素不等于0\n" +
                "答案的n, m的取值都能被32位int整型存下（即不超过2 ^ 31 - 1）。");


    }

    //公式： X(n) / Y(n) = f(n) + Y(n-1) / X(n-1)
    public int[] fraction(int[] cont) {
        // x为分子， y为分母
        int len = cont.length, x = cont[len - 1], y = 1;
        for (int i = len - 2; i >= 0; i--) {
            int temp = x;
            x = cont[i] * x + y;
            y = temp;
        }

        //找最大公因数，准备通分
        int gcd = gcd(x, y);
        if (gcd != 1) {
            x /= gcd;
            y /= gcd;
        }
        return new int[]{x, y};
    }

    private int gcd(int a, int b) {
        int rem = 0;
        while (b != 0) {
            rem = a % b;
            a = b;
            b = rem;
        }
        return a;
    }


}
