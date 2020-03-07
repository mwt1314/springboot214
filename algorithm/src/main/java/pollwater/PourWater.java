package pollwater;

import java.util.LinkedList;
import java.util.Scanner;

/**
 * 题目描述 Description
 * 有两个无刻度标志的水壶，分别可装 x 升和 y 升 （ x,y 为整数且均不大于 100 ）的水。
 * 设另有一水 缸，可用来向水壶灌水或接从水壶中倒出的水， 两水壶间，水也可以相互倾倒。
 * 已知 x 升壶为空 壶， y 升壶为空壶。问如何通过倒水或灌水操作， 用最少步数能在x或y升的壶
 * 中量出 z （ z ≤ 100 ）升的水 来。
 * <p>
 * 输入描述 Input Description
 * 一行，三个数据，分别表示 x,y 和 z;
 * <p>
 * 输出描述 Output Description
 * 一行，输出最小步数 ,如果无法达到目标，则输出"impossible"
 * <p>
 * 样例输入 Sample Input
 * 3 22 1
 * <p>
 * 样例输出 Sample Output
 * 14
 *
 * https://blog.csdn.net/G_12_M/article/details/90234535?depth_1-utm_source=distribute.pc_relevant.none-task&utm_source=distribute.pc_relevant.none-task
 *
 * @author guojm
 */

class Nodes {
    public int x;   //当前节点的A水壶所装水的体积（升）
    public int y;   //当前节点的B水壶所装水的体积（升）
    public int current; //当前节点所在层数，用于求最小步数
}

public class PourWater {

    int visited[][] = new int[100][100];  //记录该节点是否访问过
    static int a, b, c;
    Nodes n1, n2;
    Nodes t;

	public static void main(String[] args) {
		PourWater p = new PourWater();
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		a = scan.nextInt();
		b = scan.nextInt();
		c = scan.nextInt();
		if (!p.bfs(0, 0)) System.out.println("impossible");
	}

    public boolean bfs(int x, int y) {
        boolean ok = false;
        //用LinkedList模拟队列操作
        LinkedList<Nodes> q = new LinkedList<Nodes>();
        n1 = new Nodes();
        n1.x = x;
        n1.y = y;
        n1.current = 0;
        q.addLast(n1);
        while (!q.isEmpty()) {
            //取出第一个节点元素（Nodes对象）
            n2 = q.getFirst();
            //从模拟队列中移除（Nodes对象）
            Nodes x1 = q.removeFirst();

            //如果A容器或者B容器得到目标c升水
            if (n2.x == c || n2.y == c || (n2.x + n2.y == c)) {
                //输出当前结点所在的层数
                System.out.println(n2.current);
                //ok设置为true,表明倒水问题有解
                ok = true;
                break;
            }
            if (visited[n2.x][n2.y] == 1) continue; //结点重复则剪枝处理
            //当前状态没出现过，则标志已访问
            visited[n2.x][n2.y] = 1;

            for (int i = 1; i <= 6; i++) {
                t = new Nodes();

                t.current = n2.current + 1;  //记录每个入栈结点的层数

                //i=1 并且A容器中有水时，倒水操作为：将A容器中的全部水倒入水 缸
                if (i == 1 && n2.x > 0) {
                    t.x = 0;
                    t.y = n2.y;
                    q.addLast(t);
                }

                //i=2 并且B容器中有水时，倒水操作为：将B容器中的全部水倒入水 缸
                if (i == 2 && n2.y > 0) {
                    t.y = 0;
                    t.x = n2.x;
                    q.addLast(t);
                }

                //i=3 并且A容器中的水未满时，倒水操作为：将A容器装满水
                if (i == 3 && n2.x != a) {
                    t.x = a;
                    t.y = n2.y;
                    q.addLast(t);
                }

                //i=4 并且B容器中的水未满时，倒水操作为：将B容器装满水
                if (i == 4 && n2.y != b) {
                    t.y = b;
                    t.x = n2.x;
                    q.addLast(t);
                }

                //i=5并且A容器中有水，同时B容器中水未满时，倒水操作为：A容器中的水倒入B容器
                if (i == 5 && n2.x != 0 && (b - n2.y) != 0) {
                    //如果A容器中的水小于等于B容器的剩余容量
                    if (n2.x <= (b - n2.y)) {
                        t.x = 0;
                        t.y = n2.y + n2.x;
                        q.addLast(t);
                    }
                    //如果A容器中的水大于B容器的剩余容量
                    else {
                        t.y = b;
                        t.x = n2.x - (b - n2.y);
                        q.addLast(t);
                    }
                }
                //i=6并且B容器中有水，同时A容器中水未满时，倒水操作为：B容器中的水倒入A容器
                if (i == 6 && n2.y != 0 && (a - n2.x) != 0) {
                    //如果B容器中的水小于等于B容器的剩余容量
                    if (n2.y <= (a - n2.x)) {
                        t.y = 0;
                        t.x = n2.x + n2.y;
                        q.addLast(t);
                    }
                    //如果B容器中的水大于A容器的剩余容量
                    else {
                        t.x = a;
                        t.y = n2.y - (a - n2.x);
                        q.addLast(t);
                    }
                }
            }
        }
        return ok;
    }

}