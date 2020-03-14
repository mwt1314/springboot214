package leetcodecn.digui;

public class 正则表达式匹配10 {

    public static void main(String[] args) {
        System.out.println(longestCommonPrefix(new String[]{"flower", "flow", "flight"}));
    }

    public boolean isMatch(String s, String p) {
        int len1, len2;
        if (s == null || p == null) return false;
        len1 = s.length();
        len2 = p.length();
        for (int i = 0; i < len1; i++) {
            char m = s.charAt(i);
            char n = p.charAt(i);

            if (m == p.charAt(i)) {

            }

        }


        return false;
    }

    public static String longestCommonPrefix(String[] strs) {
        int len;
        if (strs == null || (len = strs.length) == 0) return "";
        String common = "";
        int minLen = strs[0].length();
        for (int i = 1; i < len; i++) {
            minLen = Math.min(minLen, strs[i].length());
        }
        boolean exist;
        for (int i = minLen; i > 0; i--) {
            exist = true;
            String cs = strs[0].substring(0, i);
            for (int j = 0; j < len; j++) {
                if (!strs[j].startsWith(cs)) {
                    exist = false;
                    break;
                }
            }
            if (exist) {
                common = cs;

                break;
            }

        }
        return common;
    }

}
