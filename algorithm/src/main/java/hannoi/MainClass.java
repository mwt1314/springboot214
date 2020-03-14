package hannoi;

public class MainClass {

    public static void main(String[] args) {
        System.out.println("已上传到CSDN");
        System.out.println("汉诺塔：移动大小盘子问题，\n" +
                "    当有n个盘子时，需要移动2^n - 1次\n" +
                "    递归的经典问题，问题详细就不多说了。\n" +
                "    求解：问题分解：\n" +
                "    (1) 将A上的N-1个盘移动到B上\n" +
                "    (2) 将A上的1个最大盘子移动到C上\n" +
                "    (3) 将B上的N-1个盘再移动到C上\n" +
                "    以上即显示了递归思想：将父问题分解为2个子问题\n" +
                "    Hanoi(1) = 1\n" +
                "    Hanoi(n) = 2*Hanoi(n-1) + 1； 其中1：盘n由A–>C的一次操作");

        int nDisks = 3;
        doTowers(nDisks, 'A', 'B', 'C');
    }

    public static void doTowers(int topN, char from, char inter, char to) {
        if (topN == 1) {
            System.out.println("Disk 1 from " + from + " to " + to);
        } else {
            doTowers(topN - 1, from, to, inter);
            System.out.println("Disk " + topN + " from " + from + " to " + to);
            doTowers(topN - 1, inter, from, to);
        }
    }
}