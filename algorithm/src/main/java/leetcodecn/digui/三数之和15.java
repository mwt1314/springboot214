package leetcodecn.digui;

import java.util.*;

public class 三数之和15 {

    public static void main(String[] args) {
        // 10 10 // 00 22

        int x = 3, y = 2, z = 2;
        System.out.println(x ^ y ^ z);


        System.out.println("15. 三数之和" +
                "给你一个包含 n 个整数的数组 nums，判断 nums 中是否存在三个元素 a，b，c ，使得 a + b + c = 0 ？请你找出所有满足条件且不重复的三元组。" +
                "注意：答案中不可以包含重复的三元组。" +
                "" +
                "示例：" +
                "给定数组 nums = [-1, 0, 1, 2, -1, -4]，" +
                "满足要求的三元组集合为：" +
                "[" +
                "  [-1, 0, 1]," +
                "  [-1, -1, 2]" +
                "]");

        List<List<Integer>> lists = threeSum(new int[]{-4, -2, 1, -5, -4, -4, 4, -2, 0, 4, 0, -2, 3, 1, -5, 0});
        for (List<Integer> list : lists) {
            System.out.println();
            list.forEach(System.out::print);
        }
    }

    public static List<List<Integer>> threeSum(int[] nums) {
        int len;
        List<List<Integer>> list = new ArrayList<>();
        if (nums == null || (len = nums.length) == 0) return list;
        int targetVal = 0, a, b, c;
        for (int i = 0; i < len; i++) {
            a = nums[i];
            for (int j = i + 1; j < len; j++) {
                b = nums[j];
                for (int k = j + 1; k < len; k++) {
                    c = nums[k];
                    if (a + b + c == targetVal && !exist(list, a, b, c)) {
                        List<Integer> l = new ArrayList<>();
                        l.add(a);
                        l.add(b);
                        l.add(c);
                        list.add(l);
                    }
                }
            }
        }
        return list;
    }

    private static boolean exist(List<List<Integer>> list, int a, int b, int c) {
        ArrayList<Integer> ss = new ArrayList<Integer>() {{
            add(a);
            add(b);
            add(c);
        }};
        Collections.sort(ss);
        for (List<Integer> kl : list) {
           Collections.sort(kl);
            if (kl.containsAll(ss)) return true;

            //    if (a != kl.get(0) || b != kl.get(1) || c != kl.get(2)) return false;
        }
        return false;
    }

}
