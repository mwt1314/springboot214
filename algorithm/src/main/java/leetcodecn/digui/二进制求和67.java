package leetcodecn.digui;

public class 二进制求和67 {

    public static void main(String[] args) {
        String a = "10100000100100110110010000010101111011011001101110111111111101000000101111001110001111100001101";
        String b = "110101001011101110001111100110001010100001101011101010000011011011001011101111001100000011011110011";
        System.out.println(addBinary(a, b));
    }

    private static String addBinary(String a, String b) {
        if (a == null || a.length() == 0) return b;
        if (b == null || b.length() == 0) return a;
        int la = a.length(), lb = b.length();
        if (la > lb) return addBinary(b, a);
        int subIndex = lb - la, index = lb - 1, prev = 0;
        StringBuilder sb = new StringBuilder();
        while (index >= 0) {
            int code = (b.charAt(index) == '1' ? 1 : 0) + prev;
            if (index - subIndex >= 0) {
                code += (a.charAt(index - subIndex) == '1' ? 1 : 0);
            }
            prev = code > 1 ? 1 : 0;
            code = ((code & 1) == 0) ? 0 : 1;
            sb.insert(0, code);
            index--;
        }
        if (prev != 0) sb.insert(0, 1);
        return sb.toString();
    }

}
