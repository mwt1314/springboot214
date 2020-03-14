package leetcodecn.digui;

public class 两两交换链表中的节点 {

    public static void main(String[] args) {
        System.out.println("已上传到CSDN");
        System.out.println("题目来源：https://leetcode-cn.com/explore/featured/card/recursion-i/256/principle-of-recursion/1201/");
        System.out.println("两两交换链表中的节点：");
        System.out.println("给定一个链表，两两交换其中相邻的节点，并返回交换后的链表。\n" +
                "你不能只是单纯的改变节点内部的值，而是需要实际的进行节点交换。\n");

        System.out.println("用尾插法创建单向链表");
        ListNode head = tailInsert();

        System.out.println("输出单向链表");
        pringListNode(head);

        System.out.println("开始两两交换了");
        ListNode node = 两两交换链表中的节点.swapPairs(head);
        pringListNode(node);
    }

    private static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

    private static ListNode swapPairs(ListNode head) {
        if (head == null || head.next == null)
            return head;
        ListNode current = head, next, prev = null, newHead = null;
        while (current != null) {
            next = current.next;
            if (next != null) {
                current.next = next.next;
                next.next = current;
                if (newHead == null) {
                    newHead = next;
                }
                if (prev != null) {
                    prev.next = next;
                }
            }
            prev = current;
            current = current.next;
        }
        return newHead;
    }

    private static ListNode swapPairs2(ListNode head) {
        ListNode tmp = new ListNode(0);  //申请一个空的节点
        tmp.next = head;  //让链表的头节点指向那个空节点的下一个节点

        ListNode temp = tmp;  //把这个空节点保存下来，用这个变量去完成交换
        while (head != null && head.next != null) {
            temp.next = head.next;
            head.next = temp.next.next;
            temp.next.next = head;
            temp = temp.next.next;  //当上面交换完了后，temp向后移两个节点。
            head = temp.next;
        }
        return tmp.next; //返回空节点之后已经交换完了的链表
    }

    public static ListNode swapPairs3(ListNode head) {
        if (head == null || head.next == null) {
            System.out.println("把节点一个一个取出来，两个两个一交换，用递归实现，");
            return head;
        }
        ListNode res = head.next; //找到交换节点的下一个
        ListNode temp = swapPairs3(head.next.next);  //需要移动头部
        res.next = head; //交换
        head.next = temp; //头部移动到下一组
        return res;
    }

    private static ListNode tailInsert() {
        ListNode head = null, tempNode = null;
        for (int i = 0; i < 4; i++) {
            ListNode node = new ListNode(i + 1);
            if (head != null) {
                tempNode.next = node;
            } else {
                head = node;
            }
            tempNode = node;
        }

        return head;
    }

    private static void pringListNode(ListNode head) {
        ListNode node = head;
        while (node != null) {
            System.out.print(node.val + "\t");
            node = node.next;
        }
        System.out.println();
    }

}
