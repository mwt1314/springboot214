package leetcodecn.digui;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class 有效的括号20 {

    public static void main(String[] args) {
        有效的括号20 x = new 有效的括号20();
        System.out.println(x.isValid("()"));
    }

    public boolean isValid(String s) {
        int len;
        if (s == null || (len = s.length()) == 0) return true;
        if ((len & 1) == 1) return false;
        Map<Character, Character> mappings = new HashMap<>();
        mappings.put(')', '(');
        mappings.put('}', '{');
        mappings.put(']', '[');

        Stack<Character> stack = new Stack<Character>() {{
            push('#');
        }};
        for (char c : s.toCharArray()) {
            if (mappings.containsKey(c)) {
                //全是右括号
                if (mappings.get(c) != stack.peek()) return false;
                stack.pop();
            } else {
                //全是左括号
                stack.push(c);
            }
        }
        return stack.size() == 1;
    }

    public boolean isValid2(String s) {
        if (s.contains("()") || s.contains("[]") || s.contains("{}")) {
            return isValid2(s.replace("()", "").replace("[]", "").replace("{}", ""));
        } else {
            return "".equals(s);
        }
    }

}
