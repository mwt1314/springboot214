package leetcodecn.digui;

import java.util.HashMap;

public class 最长回文子串5 {


    public static void main(String[] args) {
        System.out.println("题目来源：https://leetcode-cn.com/problems/longest-palindromic-substring/");
        System.out.println("5. 最长回文子串，给定一个字符串 s，找到 s 中最长的回文子串。你可以假设 s 的最大长度为 1000。" +
                "示例 1：" +
                "输入: babad" +
                "输出: bab" +
                "注意: aba 也是一个有效答案。" +

                "示例 2：" +
                "输入: cbbd" +
                "输出: bb");

        System.out.println(longestPalindrome(null));
    }

    public static String longestPalindrome(String s) {
        int len;
        if (s == null || (len = s.length()) < 2) return s;
        HashMap<Integer, String> map = new HashMap<>();
        int maxLen = 0;
        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j <= len; j++) {
                String str = s.substring(i, j);
                if (str.length() > maxLen && isPalindrome(str)) {
                    maxLen = str.length();
                    map.put(maxLen, str);
                }
            }
        }
        return map.get(maxLen);
    }

    private static boolean isPalindrome(String x) {
        String num = x;
        int len = num.length(), mid = len >>> 1;
        for (int i = 0; i < mid; i++) {
            if (num.charAt(i) != num.charAt(len - i - 1)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isPalindrome(int x) {
        if (x == 0) return true;
        if (x < 0 || x % 10 == 0) return false;
        String num = x + "";
        int len = num.length(), mid = len >>> 1;
        for (int i = 0; i < mid; i++) {
            if (num.charAt(i) != num.charAt(len - i - 1)) {
                return false;
            }
        }
        return true;
    }


}
