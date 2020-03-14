package leetcodecn.digui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class 无重复字符的最长子串3 {

    public static void main(String[] args) {
        System.out.println("题目来源：https://leetcode-cn.com/problems/longest-substring-without-repeating-characters/");
        System.out.println("3. 无重复字符的最长子串：给定一个字符串，请你找出其中不含有重复字符的最长子串的长度。" +
                "示例 1:" +
                "输入: abcabcbb" +
                "输出: 3 " +
                "解释: 因为无重复字符的最长子串是abc，所以其长度为3" +

                "示例 2:" +
                "输入: bbbbb" +
                "输出: 1" +
                "解释: 因为无重复字符的最长子串是b，所以其长度为 1" +

                "示例 3:" +
                "输入: pwwkew" +
                "输出: 3" +
                "解释: 因为无重复字符的最长子串是 wke，所以其长度为 3" +
                "     请注意，你的答案必须是子串的长度，pwke是一个子序列，不是子串。");

        //    System.out.println(lengthOfLongestSubstring2(" "));
        System.out.println(lengthOfLongestSubstring2("au"));
    }

    public static int lengthOfLongestSubstring2(String s) {
        int len;
        if (s == null || (len = s.length()) == 0) return 0;
        if (s.length() == 1) return 1;
        int maxLen = 0;
        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j <= len; j++) {
                String str = s.substring(i, j);
                if (!isRepeat(str)) {
                    maxLen = Math.max(maxLen, str.length());
                }
            }
        }
        return maxLen;
    }

    private static boolean isRepeat(String str) {
        char[] chars = str.toCharArray();
        Set<Character> set = new HashSet<>();
        for (char c : chars) {
            if (set.contains(c)) return true;
            set.add(c);
        }
        return false;
    }

    public static int lengthOfLongestSubstring(String s) {
        int len;
        if (s == null || (len = s.length()) == 0) return 0;
        int start = 0, end = 0, maxLen = 0;
        Character c;
        HashSet<Character> set = new HashSet<>();
        while (start < len && end < len) {
            c = s.charAt(end);
            if (set.contains(c)) {
                set.remove(c);
                start++;
            } else {
                set.add(c);
                end++;
            }
        }
        return maxLen;
    }

    private int x(String s) {
        int len = s.length(), start = 0, end = 1;
        int maxLen = 0;
        String max = null;
        while (start < len && end < len) {
            //    max =

        }
        return maxLen;
    }

}
