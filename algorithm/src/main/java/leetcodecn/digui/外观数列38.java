package leetcodecn.digui;

public class 外观数列38 {

    public static void main(String[] args) {
        System.out.println(countAndSay(2));
    }

    public static String countAndSay(int n) {
        String s = "1";
        for (int start = 1; start < n; start++) {
            s = make(s);
        }
        return s;
    }

    private static String make(String s) {
        StringBuilder sb = new StringBuilder();
        int i = 0, j = 0;
        for (; j < s.length(); j++) {
            char c = s.charAt(i), cj = s.charAt(j);
            if (c != cj) {
                sb.append(j - i).append(c);
                i = j;
            }
        }
        if(i != j) {
            sb.append(j - i).append(s.charAt(i));
        }
        return sb.toString();
    }

}
