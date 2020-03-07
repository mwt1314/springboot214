package pollwater;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class MyWater {

    private int SMALL_MAX_CAPACITY = 3;

    private int LARGE_MAX_CAPACITY = 4;

    private int TARGET_WATER = 5;

    private boolean success;

    public static void main(String[] args) {
        while (true) {
            new MyWater().water();
            System.out.println();
        }
    }

    public void water() {
        Node node = new Node(); //初始状态
        Scanner scan = new Scanner(System.in);
        SMALL_MAX_CAPACITY = scan.nextInt();
        LARGE_MAX_CAPACITY = scan.nextInt();
        TARGET_WATER = scan.nextInt();

        if (SMALL_MAX_CAPACITY + LARGE_MAX_CAPACITY >= TARGET_WATER) {
            pourWater(node);
        }
        if (!success) {
            System.out.println("无法实现");
        }
    }

    /**
     * 倒水的所有可能性:
     * 1.向杯子中加水              pour(null, a)
     * 2.一个杯子水倒入另一个杯子水       pour(a, b)
     * 3.杯子水倒出来              pour(a, null)
     */
    private void pourWater(Node node) {
        if (node.isRepeat()) {
            return;
        }
        if (node.isFinish()) {
            node.print();
            success = true;
            return;
        }
        List<Node> nodeList = new ArrayList<>(8);
        //向a中加水
        if (node.small < SMALL_MAX_CAPACITY) {
            nodeList.add(new Node(SMALL_MAX_CAPACITY, node.large, node));
        }
        //向b中加水
        if (node.large < LARGE_MAX_CAPACITY) {
            nodeList.add(new Node(node.small, LARGE_MAX_CAPACITY, node));
        }
        //b向a中加水
        if (node.small < SMALL_MAX_CAPACITY) {
            int transfer = Math.min(node.large, SMALL_MAX_CAPACITY - node.small);
            nodeList.add(new Node(node.small + transfer, node.large + transfer, node));
        }
        //a向b中加水
        if (node.small > 0 && node.large < LARGE_MAX_CAPACITY) {
            int transfer = Math.min(node.small, LARGE_MAX_CAPACITY - node.large);
            nodeList.add(new Node(node.small - transfer, node.large + transfer, node));
        }
        //a水导出来
        if (node.large > 0 && node.small < SMALL_MAX_CAPACITY) {
            nodeList.add(new Node(0, node.large, node));
        }
        //b水导出来
        if (node.large > 0) {
            nodeList.add(new Node(node.small, 0, node));
        }

        nodeList.forEach(this::pourWater);
        nodeList = null;
    }

    private class Node {

        public int small; //可以装3L水的杯子

        public int large; //可以装5L水的杯子

        public Node parent;

        public Node() {

        }

        public Node(int small, int large, Node parent) {
            this.small = small;
            this.large = large;
            this.parent = parent;
        }

        public boolean isFinish() { //是否完成目标
            return small + large == TARGET_WATER;
        }

        public boolean isRepeat() {
            int nowSmall = this.small;
            int nowLarge = this.large;
            Node parentNode, node = this.parent;
            while ((parentNode = node) != null) {
                if (nowSmall == parentNode.small && nowLarge == parentNode.large) {
                    return true;
                }
                node = node.parent;
            }
            return false;
        }

        @Override
        public String toString() {
            return this.small + "-" + this.large;
        }

        public void print() {
            Node node = this;
            Stack<Node> stack = new Stack<>();
            while (node != null) {
                stack.push(node);
                node = node.parent;
            }
            System.out.println("\n******************");
            while (!stack.empty()) {
                node = stack.pop();
                System.out.print(node.small + "," + node.large + "\t");
            }
        }

    }

}
