package leetcodecn.digui;

import java.util.Stack;

public class 回文链表234 {

    public static void main(String[] args) {

    }

    private static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

    public boolean isPalindrome(ListNode head) {
        ListNode node = head;
        Stack<Integer> stack = new Stack();
        int val;
        boolean begin = false;
        while (node != null) {
            val = node.val;
            if (stack.size() != 0) {
                stack.push(val);
            } else {
                if (val == stack.peek() && !begin) {
                    stack.pop();
                    begin = true;
                } else {
                    if (begin) return false;
                    stack.push(val);
                }
            }
            node = node.next;
        }
        return stack.isEmpty();
    }
}
