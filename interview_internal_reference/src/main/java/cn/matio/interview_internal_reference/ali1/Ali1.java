package cn.matio.interview_internal_reference.ali1;

/**
 * @author mawt
 * @description
 * @date 2019/12/31
 */

import java.util.Random;
import java.util.Stack;

/**
 * 如何实现一个高效的单向链表逆序输出
 * https://github.com/0voice/interview_internal_reference/blob/master/01.%E9%98%BF%E9%87%8C%E7%AF%87/1.1.1%20%E5%A6%82%E4%BD%95%E5%AE%9E%E7%8E%B0%E4%B8%80%E4%B8%AA%E9%AB%98%E6%95%88%E7%9A%84%E5%8D%95%E5%90%91%E9%93%BE%E8%A1%A8%E9%80%86%E5%BA%8F%E8%BE%93%E5%87%BA%EF%BC%9F.md
 *
 * 知识点：创建单向链表的两种方式 https://blog.csdn.net/alpgao/article/details/86509265
 * 1.头插法：将新形成的节点的下一个赋值为header,再把新形成的节点地址传给header即将header向前移动
 * 2.尾插法：相对于头插法有些许不同 因为要返回头 头不能动 所以需要一个tailer来记录最后一个值 tailer右移
 *
 */
public class Ali1 {

    private static Random random = new Random();

    public static class ListNode<T> {
        T val;

        ListNode<T> next;

        public ListNode(T val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {
        ListNode<Integer> head = headLink();
        //正序遍历单向链表
        positivePrint(head);
        //单向链表逆序输出
        reverse(head);
        positivePrint(head);
        reverse2(head);
    }

    //正序遍历单向链表
    private static void positivePrint(ListNode head) {
        if (head != null) {
            System.out.println();
            ListNode newHead = head;
            while (newHead != null) {
                System.out.print(newHead.val + "->");
                newHead = newHead.next;
            }
        }
    }

    //将单向链表逆序，改变原链表，参考https://blog.csdn.net/alpgao/article/details/86509265
    private static void reverse(ListNode head) {
        if (head == null || head.next == null) {
            return;
        }
        ListNode p = head;
        ListNode q = p.next;
        ListNode temp;
        while (q != null) {
            temp = q.next; //记下q的下一个节点
        //    p.next = null; //不能这么写，画图就明白了
            q.next = p; //正序不要了，改为逆序
            p = q; //往后移
            q = temp; //往后移
        }
        head.next = null; //防止head和第一个节点闭环
        head = p; //p就是新的头节点

    }

    //利用栈的先入后出特性实现倒序打印，并没有改变原链表
    private static void reverse2(ListNode head) {
        ListNode newNode = head;
        Stack<ListNode> stack = new Stack<>(); //利用栈的FILO特性
        while (newNode != null) {
            stack.push(newNode); //入栈
            newNode = newNode.next;
        }
        System.out.println();
        while (stack.size() > 0) {
            newNode = stack.pop(); //出栈
            System.out.print(newNode.val + "->");
        }
    }

    //头插法创建单向链表：每次生成的新节点设置为头节点
    private static ListNode<Integer> headLink() {
        ListNode<Integer> header = null;
        for (int i = 0; i < 10; i++) {
            ListNode<Integer> newNode = new ListNode<>(random());
            if (header == null) {
                header = newNode;
            } else {
                //将新节点放在header的前面
                newNode.next = header;
                //更新新节点就是头节点
                header = newNode;
            }
        }
        return header;
    }

    //尾插法创建单向链表：每次生成的新节点设置为尾节点
    private static ListNode<Integer> tailLink() {
        ListNode<Integer> header = null, tailer = null;
        for (int i = 0; i < 10; i++) {
            ListNode<Integer> newNode = new ListNode<>(random());
            if (header == null) {
                header = tailer = newNode;
            } else {
                //尾节点后面插入新节点
                tailer.next = newNode;
                //更新新节点就是尾节点
                tailer = newNode;
            }
        }
        return header;
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
