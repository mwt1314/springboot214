package queen;

public class NQueen2 {

    private static final int N = 8;

    private static int[] q = new int[N];

    private static int count = 0;

    public static void main(String[] args) {
        queen();


    }

    private static void queen() {
        int rowNum = 0, colNum = 0;

        while (rowNum < N && colNum < N) {
            q[rowNum] = colNum;


        }


        while(true) {
            for (int i = 0; i < N; i++) {
                q[rowNum] = i;  //设置皇后
            }
        }
    }

}
