package leetcodecn.digui;

import java.util.Arrays;

public class 删除排序数组中的重复项26 {

    public static void main(String[] args) {
        System.out.println(removeDuplicates(new int[]{1, 1, 1, 2, 2}));
    }

    public static int removeDuplicates(int[] nums) {
        int len = nums.length;
        int slow = 0, fast = 1;
        while (fast < len) {
            if (nums[fast] != nums[slow]) {
                slow++;
                nums[slow] = nums[fast];
            }
            fast++;
        }
        System.out.println(Arrays.toString(nums));
        return slow + 1;

        /*if (nums.length == 0) return 0;
        int i = 0;
        for (int j = 1; j < nums.length; j++) {
            if (nums[j] != nums[i]) {
                i++;
                nums[i] = nums[j];
            }
        }
        System.out.println(Arrays.toString(nums));
        return i + 1;*/


    }

}
