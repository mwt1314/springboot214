package leetcodecn.digui;

import java.util.ArrayList;
import java.util.List;
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

    public boolean isPalindrome2(ListNode head) {
        if(head != null) {
            ListNode next = head.next;
            if(!isPalindrome(next)) return false;
            char x = '1';
            int num = (int)x - (int)('0');

        }


        return true;
    }

    public boolean isPalindrome(ListNode head) {
        ListNode node = head;
        List<Integer> list = new ArrayList();
        while (node != null) {
            list.add(node.val);
            node = node.next;
        }
        int size = list.size();
        for (int i = 0; i < (size >>> 1); i++) {
            if (!list.get(i).equals(list.get(size - i - 1))) {
                return false;
            }
        }
        return true;
    }
}
