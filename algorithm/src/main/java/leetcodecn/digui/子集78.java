package leetcodecn.digui;

import java.util.ArrayList;
import java.util.List;

public class 子集78 {

    public static void main(String[] args) {

    }

    public static List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> list = new ArrayList();
        int len;
        if (nums == null || (len = nums.length) == 0) return list;
        for (int i = 0; i < len; i++) {
            List<Integer> nl = new ArrayList<>();
            for (int j = 0; j < len; j++) {
                for (int k = 0; k < len; k++) {

                }
            }
        }
        return list;
    }

}
