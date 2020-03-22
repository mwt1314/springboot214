package leetcodecn.digui;

import java.util.ArrayList;
import java.util.Stack;

public class 反转链表206 {

    private static class ListNode {
     int val;
     ListNode next;
     ListNode(int x) { val = x; }
 }

    public ListNode reverseList(ListNode head) {
        ArrayList list1 = new ArrayList();
        Object remove = list1.remove(-1);

        Stack<Integer> stack = new Stack();
        ListNode node = head;
        while(node != null) {
            stack.push(node.val);
            node = node.next;
        }
        ListNode list = new ListNode(-1), newHead = list;
        while (!stack.isEmpty()) {
            ListNode newNode = new ListNode(stack.pop());
            list.next = newNode;
            list = newNode;
        }
        return newHead.next;

    }

}
