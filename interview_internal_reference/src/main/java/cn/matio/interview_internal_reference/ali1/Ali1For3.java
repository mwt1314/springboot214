package cn.matio.interview_internal_reference.ali1;

/**
 * 双向链表
 */
public class Ali1For3 {

    private static DoubleListNode head;

    private static class DoubleListNode<T> {
        T val;
        DoubleListNode next;
        DoubleListNode prev;
        public DoubleListNode(T val) {
            this.val = val;
        }
    }

    private static void headInsert() {
        DoubleListNode next = null;
        for (int i = 0; i < 10; i++) {
            DoubleListNode<Integer> newNode = new DoubleListNode(i);
            if (head == null) {
                head = newNode;
            } else {
                next = head.next;
                if (next != null) {
                    next.prev = newNode;
                }
                newNode.next = next;
                head.next = newNode;
                newNode.prev = head;
            }
        }
    }

    private static void print() {
        if (head == null) {
            return;
        }
        System.out.println();
        DoubleListNode curNode = head;
        while (curNode != null) {
            System.out.print(curNode.val + "\t");
            curNode = curNode.next;
        }
    }

    private static void tailInsert() {
        DoubleListNode tail = null;
        for (int i = 0; i < 10; i++) {
            DoubleListNode<Integer> newNode = new DoubleListNode(i);
            if (head == null) {
                head = tail = newNode;
            } else {
                tail.next = newNode;
                newNode.prev = tail;
                tail = newNode;
            }
        }
    }

    public static void main(String[] args) {
        headInsert();
        print();
    }


}
