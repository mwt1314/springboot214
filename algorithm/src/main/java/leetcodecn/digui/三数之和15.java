package leetcodecn.digui;

import java.util.*;

public class 三数之和15 {

    public static void main(String[] args) {
        System.out.println("15. 三数之和：给你一个包含 n 个整数的数组 nums，判断 nums 中是否存在三个元素 a，b，c ，" +
                "使得 a + b + c = 0 ？请你找出所有满足条件且不重复的三元组。" +
                "注意：答案中不可以包含重复的三元组。" +
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

    private static List<List<Integer>> threeSum(int[] nums) {
        int len;
        List<List<Integer>> list = new ArrayList<>();
        if (nums == null || (len = nums.length) == 0) return list;
        int targetVal = 0, sum, left, right;
        Arrays.sort(nums);
        for (int i = 0; i < len; i++) {
            if (nums[i] > targetVal) break;
            left = i + 1;
            right = len - 1;
            while (left < right) {
                if (left > i + 1 && nums[left] == nums[left - 1]) {// 如果当前值与前一个相同，则跳过，保证不重复。
                    // left > i + 1保证st位置的数和i位置的数不是一个
                    left++;
                    continue;
                }
                if ((sum = nums[i] + nums[left] + nums[right]) == targetVal) {
                    list.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    while (left < right && nums[left] == nums[left + 1]) {
                        left++;
                    }
                    while (left < right && nums[right] == nums[right - 1]) {
                        right--;
                    }
                    left++;
                    right--;
                } else if (sum < targetVal) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        return list;
    }


    private static List<List<Integer>> threeSum2(int[] nums) {
        int len;
        List<List<Integer>> list = new ArrayList<>();
        if (nums == null || (len = nums.length) == 0) return list;
        int targetVal = 0;
        for (int i = 0; i < len; i++) {
            int twoSum = targetVal - nums[i];
            Map<Integer, Integer> map = new HashMap<>();
            for (int j = i + 1; j < len; j++) {
                if (map.containsKey(twoSum - nums[j])) {
                    list.add(Arrays.asList(nums[i], nums[j], twoSum - nums[j]));
                } else {
                    map.put(nums[j], j);
                }
            }
        }
        return list;
    }


}
