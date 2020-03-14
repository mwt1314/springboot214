package leetcodecn.digui;

import java.util.HashMap;
import java.util.Map;

public class 两数之和1 {

    public static void main(String[] args) {
        System.out.println("题目来源：https://leetcode-cn.com/problems/two-sum/solution/");
        System.out.println("1. 两数之和：给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那两个整数，" +
                "并返回他们的数组下标。" +
                "你可以假设每种输入只会对应一个答案。但是，你不能重复利用这个数组中同样的元素");
    }

    public int[] twoSum(int[] nums, int target) {
        if (nums == null || nums.length < 2) return null;
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(target - nums[i])) {
                return new int[]{map.get(target - nums[i]), i};
            }
            map.put(nums[i], i);
        }
        return null;
    }

    public int[] twoSum2(int[] nums, int target) {
        System.out.println("方法1：暴力法，遍历每个元素 x，并查找是否存在一个值与 target - x相等的目标元素");
        System.out.println("复杂度分析：" +
                "时间复杂度：O(n^2)，对于每个元素，我们试图通过遍历数组的其余部分来寻找它所对应的目标元素，这将耗费 O(n)的时间。因此时间复杂度为 O(n^2)" +
                "空间复杂度：O(1)O(1)。");
        if (nums == null || nums.length < 2) return null;
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[] { i, j };

                }
            }
        }
        return null;
    }

}
