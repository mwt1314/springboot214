package hannoi;

import java.util.*;

public class Hanoi {

    static Vector<Integer> V[] = new Vector[3];
    static Vector<Integer> ans[][] = new Vector[100][3];
    static int L, nowStep, maxStep;

    static void Move(int n, int a, int c) {
        int t = V[a].lastElement();
        V[a].removeElementAt(V[a].size() - 1);
        V[c].add(t);
        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < V[j].size(); k++) {
                ans[nowStep][j].add(V[j].get(k));
            }
        }
        nowStep++;
    }

    static void dfs(int n, int a, int b, int c) {
        if (n == 0) {
            return;
        }
        dfs(n - 1, a, c, b);
        Move(n, a, c);
        dfs(n - 1, b, a, c);
    }

    public void Solve() {
        dfs(L, 0, 1, 2);

//        for(int i = 0;i <= maxStep;i ++) {
//            System.out.println("------------------");
//            System.out.println(i);
//            for(int j = 0;j < 3;j ++) {
//                for(int k = 0;k < ans[i][j].size();k ++) {
//                    System.out.print(ans[i][j].get(k) + " ");
//                }
//                System.out.println();
//            }
//            System.out.println("------------------");
//        }

    }

    public Hanoi(int x) {
        this.L = x;
        maxStep = (int) Math.pow(2, L) - 1;
        nowStep = 0;
        for (int i = 0; i < 3; i++) {
            V[i] = new Vector<Integer>();
            V[i].clear();
        }
        for (int i = 0; i < 90; i++) {
            ans[i][0] = new Vector<Integer>();
            ans[i][0].clear();
            ans[i][1] = new Vector<Integer>();
            ans[i][1].clear();
            ans[i][2] = new Vector<Integer>();
            ans[i][2].clear();
        }
        for (int i = L; i >= 1; i--) {
            V[0].add(i);
            ans[nowStep][0].add(i);
        }
        nowStep++;
    }

}