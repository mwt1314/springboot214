package leetcodecn.digui;

import java.util.HashMap;
import java.util.Map;

public class 单词规律290 {

    public static void main(String[] args) {
        System.out.println(wordPattern("abba", "dog cat cat fish"));
    }

    private static boolean wordPattern(String pattern, String str) {
        String[] ss = str.split(" ");
        int len = pattern.length();
        if (len != ss.length) return false;
        Map<Character, String> map = new HashMap();
        for (int i = 0; i < len; i++) {
            String cs = ss[i];
            char c = pattern.charAt(i);
            if (map.containsKey(c) && !map.get(c).equals(cs)) return false;
            if (!map.containsKey(c) && map.containsValue(cs)) return false;
            map.put(c, cs);
        }
        return true;
    }

}
