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

        System.out.println(lengthOfLongestSubstring("au"));
    }

    public static int lengthOfLongestSubstring(String s) {
        int n = s.length();
        Set<Character> set = new HashSet<>();
        int ans = 0, i = 0, j = 0;
        while (i < n && j < n) {
            if (!set.contains(s.charAt(j))) {
                set.add(s.charAt(j++));
                ans = Math.max(ans, j - i);
            } else {
                set.remove(s.charAt(i++));
            }
        }
        return ans;
    }

}
