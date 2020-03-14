package leetcodecn.digui;

public class Excel表列名称168 {

    public static void main(String[] args) {
        System.out.println("题目来源：https://leetcode-cn.com/problems/excel-sheet-column-title/");
        System.out.println("168. Excel表列名称：给定一个正整数，返回它在 Excel 表中相对应的列名称。");
        System.out.println("给定一个正整数，返回它在 Excel 表中相对应的列名称。" +
                "例如，" +
                "    1 -> A" +
                "    2 -> B" +
                "    3 -> C" +
                "    ..." +
                "    26 -> Z" +
                "    27 -> AA" +
                "    28 -> AB " +
                "    ...");
        System.out.println("逢26进1");
        System.out.println(convertToTitle(1));
        System.out.println(convertToTitle(53));
    }

    public static String convertToTitle(int n) {
        if (n > 0) {
            char[] cs = new char[26];
            for (int i = 0; i < 26; i++) {
                cs[i] = (char) (65 + i);
            }
            if (n < 27) return cs[n - 1] + "";
            StringBuilder sb = new StringBuilder();
            int cha = n;
            while (cha > 26) {
                sb.append("A");
                cha -= 26;
            }
            if (cha > 0) {
                sb.append(cs[cha - 1]);
            }
            return sb.toString();
        }
        return null;
    }

}
