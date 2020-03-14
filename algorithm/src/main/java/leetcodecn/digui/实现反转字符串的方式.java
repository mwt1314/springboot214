package leetcodecn.digui;

import java.util.Arrays;

public class 实现反转字符串的方式 {

    public static void main(String[] args) {
        System.out.println("已上传到CSDN");
        System.out.println("题目来源：https://leetcode-cn.com/explore/featured/card/recursion-i/256/principle-of-recursion/1198/");
        System.out.println("实现反转字符串的方式：");

        System.out.println("1.递归反转字符串");
        String str = "abcdefg";
        System.out.println(reverse(str));

        System.out.println("2.递归反转字节数组");
        char[] chars = {'a', 'b', 'c', 'a', 'e'};
        System.out.println(reverse(chars, chars.length - 1));

        System.out.println("3.for循环反转字节数组");
        reverseString(chars);
        System.out.println(Arrays.toString(chars));
    }

    private static String reverse(String str) {
        if (str.length() == 1) return str;
        return str.charAt(str.length() - 1) + reverse(str.substring(0, str.length() - 1));
    }

    private static String reverse(char[] str, int start) {
        if (start == 0) return str[start] + "";
        return str[start--] + reverse(str, start);
    }

    public static  void reverseString(char[] s) {
        int len;
        if (s == null || (len = s.length) < 2) return;
        char temp;
        for (int i = 0; i < (len >>> 1); i++) {
            temp = s[i];
            s[i] = s[len - i - 1];
            s[len - i - 1] = temp;
        }
    }

}
