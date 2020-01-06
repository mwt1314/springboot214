package cn.matio.interview_internal_reference.ali1;

/**
 * 01.阿里篇/1.3.2 给定一个链表，删除链表的倒数第N个节点，并且返回链表的头结点.md
 * <p>
 * 题目：给定一个链表，删除链表的倒数第 N 个节点，并且返回链表的头结点。
 * ◼ 示例： 给定一个链表: 1->2->3->4->5, 和 n = 2. 当删除了倒数第二个节点后，链表变为 1->2->3->5.
 * 说明： 给定的 n 保证是有效的。
 * 要求： 只允许对链表进行一次遍历。
 */
public class Ali132 {

    private static Node head;

    private static class Node<T> {
        T val;
        Node next;

        Node(T val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {
        jinzhi();
        removeN(3);
    }

    private static void jinzhi() {
        System.out.println(Integer.toUnsignedString(235, 15));
        int num = 235;
        int jz = 15;
        int yushu = 0;
        StringBuilder result = new StringBuilder();
        while (num >= jz) {
            yushu = num % jz;
            result.insert(0, yushu);
            num = num / jz;
        }
        System.out.println(result);
    }



    private static void removeN(int n) {
        if (head == null) {
            tailInsert();
        }

    }

    private static void tailInsert() {
        Node tail = null;
        for (int i = 0; i < 10; i++) {
            Node newNode = new Node(i);
            if (head == null) {
                head = tail = newNode;
            } else {
                tail.next = newNode;
                tail = newNode;
            }
        }
    }

}
