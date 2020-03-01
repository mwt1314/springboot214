package queen;

import java.util.Arrays;

public class NQueen {

    private static final int N = 8;

    private static int[] q = new int[N];

    private static int count = 0;

    public static void main(String[] args) {
        queen(0);
        System.out.println(count);
    }

    /**
     * 开始排皇后了
     *
     * @param rowNum 行号 从0开始
     */
    private static void queen(int rowNum) {
        for (int i = 0; i < N; i++) {
            q[rowNum] = i;
            if (canQueen(rowNum)) {
                if (rowNum >= N - 1) {
                    count++;
                    System.out.println(Arrays.toString(q));
                    return;
                }
                queen(rowNum + 1);
            }
        }

    }

    /**
     * 判断是否在同一行，同一列，同对角线
     *
     * @param rowNum 行号 从0开始，表示现在排到哪一行了
     */
    private static boolean canQueen(int rowNum) {
        int x, y;
        for (int i = 0; i < rowNum; i++) {
          /*  if ((x = q[i]) == (y = q[rowNum])   //保证了不在同一列
                    || (i == x && rowNum == y)  //保证不在同一对角线
                    || (((i + x == N - 1) && (rowNum + y == N - 1)))) {
                return false;
            }
*/
            if((q[i] == q[rowNum]) || (Math.abs(i-rowNum) == Math.abs(q[i]-q[rowNum]))) {
                return false;
            }

        }
        return true;
    }

}
