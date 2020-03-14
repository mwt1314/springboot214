package leetcodecn.digui;

import java.util.*;

public class 实现帕斯卡三角PascalsTriangle {

    public static void main(String[] args) {
        System.out.println("已上传到CSDN");
        System.out.println("题目来源：https://leetcode-cn.com/explore/featured/card/recursion-i/257/recurrence-relation/1208/");
        System.out.println("递归实现帕斯卡三角（Pascal's Triangle）");

        实现帕斯卡三角PascalsTriangle pascalsTriangle = new 实现帕斯卡三角PascalsTriangle();
        System.out.println("递归实现求帕斯卡三角任意维度的值");
        int i = 5, j = 4;
        System.out.println("f(" + i + "," + j + ")=" + pascalsTriangle.pascal(i, j));

        System.out.println("for循环实现帕斯卡三角");
        System.out.println(pascalsTriangle.pascalTail(1, 1, i, j, 0));


        System.out.println("逻辑简单直观地打印任意高度的帕斯卡三角");
        pascalsTriangle.pascalFor(10);

        System.out.println("ps3");
        pascalsTriangle.ps3(10);

        List<Integer> list = new LinkedList<>();
        list.add(1);
        System.out.println(list);
        //输出几行
        int row = 5;
        test7(list, row);
    }

    private long pascal(int row, int col) {
        if (col == 1 || row == col) return 1;
        return pascal(row - 1, col - 1) + pascal(row - 1, col);
    }

    private long pascalTail(int startRow, int startCol, int endRow, int endCol, long result) {
        if (endCol == 1 || endCol == endRow) return result;
        return 0L;
    }

    private void pascalFor(int row) {
        int[][] arr = new int[row][row];
        for (int i = 0; i < row; i++) {
            arr[i][0] = 1;    //帕斯卡三角的特点
            arr[i][i] = 1;    //帕斯卡三角的特点
        }
        for (int i = 2; i < row; i++) {
            for (int j = 1; j < row; j++) {
                arr[i][j] = arr[i - 1][j - 1] + arr[i - 1][j];  //帕斯卡三角的特点
            }
        }

        for (int i = 0; i < row; i++) {
            /*for (int j = i; j < row; j++) {
                System.out.print(" ");
            }*/
            System.out.format("%" + (row - i) * 2 + "s", "");   //代替for循环打印空格

            for (int j = 0; j < i + 1; j++) {
                //    System.out.print(arr[i][j] + " ");
                System.out.format("%4d", arr[i][j]);
            }
            System.out.println();

            Map<Integer, List<Integer>> map = new HashMap();
            Iterator<Map.Entry<Integer, List<Integer>>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, List<Integer>> entry = iterator.next();
                Integer key = entry.getKey();
                List<Integer> values = entry.getValue();


            }
        }
    }

    private void ps3(int row) {
        for (int i = 0; i < row; i++) {
            System.out.format("%" + (row - i) * 2 + "s", "");     //代替for循环打印空格

            int number = 1; //帕斯卡三角的特点：第一列都是1
            for (int j = 0; j <= i; j++) {
                System.out.format("%4d", number);
                number = number * (i - j) / (j + 1);
            }
            System.out.println();
        }
    }

    private static List<Integer> test7(List<Integer> list, int i) {
        if (i == 0) {
            return null;
        }
        i--;
        List<Integer> result = new LinkedList<Integer>();
        //在最前面补位1
        result.add(1);
        for (int j = 0; j < list.size(); j++) {
            //每次相加，判断加到传进来的源的最大size的时候，就不加下一个，不然出现游标问题
            if (j == list.size() - 1) {
//				System.out.println(list.get(j));
                result.add(list.get(j));
                break;
            }
            //实现题目中对应位置的两个相加
            if (j <= list.size()) {
//				System.out.println(list.get(j)+list.get(j+1));
                result.add(list.get(j) + list.get(j + 1));
            }
        }

        System.out.println(result);
        test7(result, i);
        return result;
    }
    public int[] plusOne(int[] digits) {
        int len = digits.length;
        int add = 1, prev = 0;

        for(int i = len - 1; i >= 0; i--) {
            digits[i] = (digits[i] + add + prev) % 10;
            prev = (digits[i] + add) / 10;
            if (prev == 0) {
                break;
            }
        }
        if(prev == 1) {
            int[] newDigits = new int[len + 1];
            newDigits[0] = prev;
            System.arraycopy(digits, 0, newDigits, 1, len);
            return newDigits;
        }
        List<Integer> list1 = new ArrayList();
       


        return digits;
    }
}