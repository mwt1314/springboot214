package leetcodecn.digui;

public class 最长公共前缀14 {

    public static void main(String[] args) {
        System.out.println(" 最长公共前缀\n" +
                "编写一个函数来查找字符串数组中的最长公共前缀。\n" +
                "如果不存在公共前缀，返回空字符串 \"\"。");
    }

    public String longestCommonPrefix(String[] strs) {
        if (strs == null || strs.length == 0) return "";
        String prefix = strs[0];    //假设第一个元素就是公共前缀
        for (int i = 1; i < strs.length; i++)
            while (strs[i].indexOf(prefix) != 0) {  //
                prefix = prefix.substring(0, prefix.length() - 1);
                if (prefix.isEmpty()) return "";
            }
        return prefix;
    }

    public String longestCommonPrefix2(String[] strs) {
        if (strs == null || strs.length == 0) return "";
        String prefix = strs[0];
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            for (int j = 1; j < strs.length; j++) {
                if (c != strs[j].charAt(i) || i != strs[j].length()) {
                    return prefix.substring(0, i);
                }
            }
        }
        return prefix;
    }

    public String longestCommonPrefix3(String[] strs) {
        if (strs == null || strs.length == 0) return "";
        return longestCommonPrefix(strs, 0, strs.length - 1);
    }

    private String longestCommonPrefix(String[] strs, int low, int high) {
        if (low == high) return strs[low];
        int mid = (low + high) >>> 1;
        String leftStr = longestCommonPrefix(strs, low, mid);
        String rightStr = longestCommonPrefix(strs, mid, high);
        return commonPrefix(leftStr, rightStr);
    }

    private String commonPrefix(String leftStr, String rightStr) {
        int minLen = Math.min(leftStr.length(), rightStr.length());
        for (int i = 0; i < minLen; i++) {
            if (leftStr.charAt(i) != rightStr.charAt(i)) {
                return leftStr.substring(0, i);
            }
        }
        return leftStr.substring(0, minLen);
    }

    public String longestCommonPrefix4(String[] strs) {
        if (strs == null || strs.length == 0)
            return "";
        int minLen = Integer.MAX_VALUE;
        for (String str : strs)
            minLen = Math.min(minLen, str.length());
        int low = 1;
        int high = minLen;
        while (low <= high) {
            int middle = (low + high) / 2;
            if (isCommonPrefix(strs, middle))
                low = middle + 1;
            else
                high = middle - 1;
        }
        return strs[0].substring(0, (low + high) / 2);
    }

    private boolean isCommonPrefix(String[] strs, int len) {
        String str1 = strs[0].substring(0, len);
        for (int i = 1; i < strs.length; i++)
            if (!strs[i].startsWith(str1))
                return false;
        return true;
    }

}
