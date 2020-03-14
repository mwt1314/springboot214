package leetcodecn.digui;

import java.util.Arrays;

public class 移除元素27 {

    public static void main(String[] args) {
        System.out.println("题目来源：https://leetcode-cn.com/problems/remove-element/");
        System.out.println("27. 移除元素：给你一个数组 nums 和一个值 val，你需要原地移除所有数值等于val的元素，" +
                "并返回移除后数组的新长度。" +
                "不要使用额外的数组空间，你必须仅使用 O(1) 额外空间并 原地 修改输入数组。" +
                "元素的顺序可以改变。你不需要考虑数组中超出新长度后面的元素。" +
                "示例 1:" +
                "给定 nums = [3,2,2,3], val = 3," +
                "函数应该返回新的长度 2, 并且 nums 中的前两个元素均为 2。" +
                "你不需要考虑数组中超出新长度后面的元素。" +
                "示例 2:" +
                "给定 nums = [0,1,2,2,3,0,4,2], val = 2," +
                "函数应该返回新的长度 5, 并且 nums 中的前五个元素为 0, 1, 3, 0, 4。" +
                "注意这五个元素可为任意顺序。" +
                "你不需要考虑数组中超出新长度后面的元素。");
        
        int[] nums = {1, 3,2,2,3};
        int val = 3;
        System.out.println(removeElement(nums, val));;
        System.out.println(Arrays.toString(nums));
        System.out.println(searchInsert(new int[]{1, 3, 5, 6}, 2));
    }

    public static int removeElement(int[] nums, int val) {
        int start = 0;
        int end = nums.length - 1;
        while (start < end) {
            if (nums[start] == val) {
                nums[start] = nums[end --];
            } else {
                start++;
            }
        }
        return end;
    }

    public static int searchInsert(int[] nums, int target) {
        int len = 0;
        if (nums == null || (len = nums.length) == 0) return 0;
        int low = 0, high = len - 1, mid;
        while (low < high) {
            mid = (low + high) >>> 1;
            System.out.println("low=" + low + ",mid=" + mid + ",high=" + high);
            if(nums[mid] == target) {
                return mid;
            } else if(nums[mid] < target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

}
