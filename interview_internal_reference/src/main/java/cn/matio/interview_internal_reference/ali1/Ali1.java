package cn.matio.interview_internal_reference.ali1;

import java.util.Random;
import java.util.Stack;

public class Ali1 {

    private static Random random = new Random();

    private static class ListNode<T> {
        T val;

        ListNode<T> next;

        public ListNode(T val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {
        System.out.println("已上传到CSDN");
        System.out.println("/**\n" +
                " * 如何实现一个高效的单向链表逆序输出\n" +
                " * https://github.com/0voice/interview_internal_reference/blob/master/01.%E9%98%BF%E9%87%8C%E7%AF%87/1.1.1%20%E5%A6%82%E4%BD%95%E5%AE%9E%E7%8E%B0%E4%B8%80%E4%B8%AA%E9%AB%98%E6%95%88%E7%9A%84%E5%8D%95%E5%90%91%E9%93%BE%E8%A1%A8%E9%80%86%E5%BA%8F%E8%BE%93%E5%87%BA%EF%BC%9F.md\n" +
                " * <p>\n" +
                " * 知识点：\n" +
                " * 链表是一种物理存储结构上非连续、非顺序的存储结构，数据元素的逻辑顺序是通过链表中的指针链接次序实现的\n" +
                " *\n" +
                " * 链表的特点是什么？\n" +
                " * 获取数据麻烦，需要遍历查找，比数组慢\n" +
                " * 方便插入、删除\n" +
                " *\n" +
                " * 创建单向链表的两种方式 https://blog.csdn.net/alpgao/article/details/86509265\n" +
                " * 1.头插法：将新形成的节点的下一个赋值为header,再把新形成的节点地址传给header即将header向前移动\n" +
                " * 2.尾插法：相对于头插法有些许不同 因为要返回头 头不能动 所以需要一个tailer来记录最后一个值 tailer右移\n" +
                " */");

        //头插法生成单向链表
        ListNode<Integer> head = headLink();
        //正序遍历单向链表
        positivePrint(head);
        //单向链表逆序输出
        //    reverse(head);
        head = reverse1(head);
        positivePrint(head);

        positivePrint(reverse2(head));
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

    // 在不使用额外存储节点的情况下使一个单链表的所有节点逆序
    // 循环式 https://blog.csdn.net/xiao_ma_CSDN/article/details/80550092
    private static ListNode reverse1(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode curHead = head, prev = null, next = null;
        while (curHead != null) {
            //记录下当前节点的下一个节点
            next = curHead.next;
            //逆序
            curHead.next = prev;
            //后移
            prev = curHead;
            //后移
            curHead = next;
        }
        return prev;
    }

    //在不使用额外存储节点的情况下使一个单链表的所有节点逆序
    //递归方式https://blog.csdn.net/xiao_ma_CSDN/article/details/80550092
    private static ListNode reverse2(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode newHead = reverse2(head.next);
        //另当前节点的下一个节点指向当前节点，形成闭合回路
        head.next.next = head;
        //断开闭合回路，实现逆序
        head.next = null;
        return newHead;
    }

    //利用栈的先入后出特性实现倒序打印，并没有改变原链表
    private static void reverse3(ListNode head) {
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

    //链表入栈
    private static void reverse4(ListNode head) {
        if (head == null || head.next == null) {
            return;
        }
        ListNode curNode = head, next = null;
        Stack<ListNode> stack = new Stack<>();
        while (curNode != null) {
            //将当前节点入栈
            stack.push(curNode);
            //找到next节点
            next = curNode.next;
            //断开下一个节点
            curNode.next = null;
            //后移
            curNode = next;
        }

        head = stack.pop();
        curNode = head;
        while (!stack.isEmpty()) {
            curNode.next = stack.pop();
            curNode = curNode.next;
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
