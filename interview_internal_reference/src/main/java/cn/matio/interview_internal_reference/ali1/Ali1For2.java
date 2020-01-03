package cn.matio.interview_internal_reference.ali1;

import java.util.List;
import java.util.Random;

/**
 *
 */
public class Ali1For2 {

    private static Random random = new Random();

    private static class ListNode<T> {
        T val;

        ListNode<T> next;

        public ListNode() {
        }

        public ListNode(T val) {
            this.val = val;
        }
    }

    //将单向链表逆序，改变原链表，参考https://blog.csdn.net/alpgao/article/details/86509265
    //借助头结点
    private static ListNode reverse() {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode p = head.next, q = p.next, next = null;
        p.next = null;
        while (q != null) {
            next = q.next;
            q.next = p;
            p = q;
            q = next;
        }
        head.next = p;
        return head;
    }

    //特殊，头结点不存储任何数据
    private static ListNode head;

    public static void main(String[] args) {
        //头插法创建单向链表
        //    headInsert();
        //尾插法创建单向链表
        tailInsert();
        //打印链表
        print();

        //逆序
        reverse();
        //打印链表
        print();
    }

    private static void print() {
        if (head == null || head.next == null) {
            return;
        }
        System.out.println();
        ListNode cuNode = head.next;
        while (cuNode != null) {
            System.out.print(cuNode.val + "\t");
            cuNode = cuNode.next;
        }
    }

    //头插法
    private static void headInsert() {
        if (head == null) {
            head = new ListNode();  //特殊，头结点不存储任何数据
        }
        for (int i = 0; i < 10; i++) {
            ListNode newNode = new ListNode(random());
            newNode.next = head.next;
            head.next = newNode;
        }
    }

    //尾插法
    private static void tailInsert() {
        ListNode tail = null;
        if (head == null) {
            head = new ListNode();  //特殊，头结点不存储任何数据
            tail = head;
        }
        for (int i = 0; i < 10; i++) {
            ListNode newNode = new ListNode(random());
            tail.next = newNode;
            tail = newNode;
        }
    }

    /**
     * 生成[1,10]随机数
     *
     * @return
     */
    private static int random() {
        return random.nextInt(10) + 1;
    }

}
