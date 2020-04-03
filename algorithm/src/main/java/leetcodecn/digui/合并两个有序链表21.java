package leetcodecn.digui;

public class 合并两个有序链表21 {

    public static void main(String[] args) {
        System.out.println("将两个升序链表合并为一个新的升序链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。 \n" +
                "示例：\n" +
                "输入：1->2->4, 1->3->4\n" +
                "输出：1->1->2->3->4->4\n");
        合并两个有序链表21 x = new 合并两个有序链表21();
        x.mergeTwoLists(null, null);
    }

    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode m = l1, n = l2;
        ListNode head = new ListNode(-1), temp = head, node;
        boolean whomin;
        while (m != null || n != null) {
            whomin = n == null || (m != null && m.val <= n.val);
            node = new ListNode(whomin ? m.val : n.val);
            temp.next = node;
            temp = node;
            if (m == null || !whomin) {
                n = n.next;
            } else {
                m = m.next;
            }
        }
        return head.next;
    }

    public ListNode mergeTwoLists2(ListNode l1, ListNode l2) {
        if (l1 == null) return l2;
        if (l2 == null) return l1;
        if (l1.val < l2.val) {
            l1.next = mergeTwoLists2(l1.next, l2);
            return l1;
        }
        l2.next = mergeTwoLists2(l2.next, l1);
        return l2;
    }

}
