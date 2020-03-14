package leetcodecn.digui;

import java.util.HashMap;

public class 多数元素169 {

    public static void main(String[] args) {
        System.out.println("题目来源：https://leetcode-cn.com/problems/majority-element/");
        System.out.println("169. 多数元素：给定一个大小为n的数组，找到其中的多数元素。" +
                "多数元素是指在数组中出现次数大于n/2的元素。" +
                "你可以假设数组是非空的，并且给定的数组总是存在多数元素");

    }

    public static int majorityElement(int[] nums) {
        HashMap<Integer, Integer> map = new HashMap<>();
        int len = nums.length, num, times, mid = len >>> 1;
        if(len < 2) return nums[0];
        for (int i = 0; i < len; i++) {
            num = nums[i];
            if (map.containsKey(num)) {
                times = map.get(num) + 1;
                map.put(num, times);
                if (times > mid) {
                    return num;
                }
            } else {
                map.put(num, 1);
            }
        }
        return 0;
    }


}
