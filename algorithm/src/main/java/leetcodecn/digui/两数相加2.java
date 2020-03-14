package leetcodecn.digui;

public class 两数相加2 {

    public static void main(String[] args) {
        System.out.println("题目来源：https://leetcode-cn.com/problems/add-two-numbers/");
        System.out.println("2. 两数相加：给出两个 非空 的链表用来表示两个非负的整数。其中，它们各自的位数是按照逆序的方式存储的，" +
                "并且它们的每个节点只能存储 一位 数字。如果，我们将这两个数相加起来，则会返回一个新的链表来表示它们的和。\n" +
                "您可以假设除了数字 0 之外，这两个数都不会以 0 开头。\n" +
                "示例：\n" +
                "输入：(2 -> 4 -> 3) + (5 -> 6 -> 4)\n" +
                "输出：7 -> 0 -> 8\n" +
                "原因：342 + 465 = 807\n");
    }

    private static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }


    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode n1 = l1, n2 = l2;
        int num1, num2, num, prev = 0;
        ListNode numNode = null, prevNode = null, head = numNode;
        while (n1 != null || n2 != null) {
            num1 = num2 = 0;
            if (n1 != null) {
                num1 = n1.val;
                n1 = n1.next;
            }
            if (n2 != null) {
                num2 = n2.val;
                n2 = n2.next;
            }
            num = num1 + num2 + prev;
            prev = num / 10;
            numNode = new ListNode(num % 10);
            if (prevNode != null) {
                prevNode.next = numNode;
            }
            if (head == null) head = numNode;
            prevNode = numNode;
        }
        if (prev == 1) {
            numNode = new ListNode(1);
            prevNode.next = numNode;
        }
        return head;
    }

    public ListNode addTwoNumbers2(ListNode l1, ListNode l2) {
        ListNode dummyHead = new ListNode(0);
        ListNode p = l1, q = l2, curr = dummyHead;
        int carry = 0;
        while (p != null || q != null) {
            int x = (p != null) ? p.val : 0;
            int y = (q != null) ? q.val : 0;
            int sum = carry + x + y;
            carry = sum / 10;
            curr.next = new ListNode(sum % 10);
            curr = curr.next;
            if (p != null) p = p.next;
            if (q != null) q = q.next;
        }
        if (carry > 0) {
            curr.next = new ListNode(carry);
        }
        return dummyHead.next;
    }


}
