package leetcodecn.digui;

import java.util.Arrays;

public class 缺失数字268 {

    public static void main(String[] args) {
        int[] nums = new int[]{9, 6, 4, 2, 3, 5, 7, 0, 1};
        System.out.println(missingNumber(nums));
        System.out.println(missingNumber2(nums));
        System.out.println(missingNumber3(nums));
        System.out.println(missingNumber4(nums));
    }

    private static int missingNumber(int[] nums) {
        int len = nums.length;
        int sum = len * (len + 1) / 2;
        for (int num : nums) {
            sum -= num;
        }
        return sum;
    }

    private static int missingNumber2(int[] nums) {
        Arrays.sort(nums);
        if (0 != nums[0]) return 0;
        if (nums.length != nums[nums.length - 1]) return nums.length;
        for (int i = 0; i < nums.length; i++) {
            if (i != nums[i]) {
                return i;
            }
        }
        return 0;
    }

    private static int missingNumber3(int[] nums) {
        //由于异或运算（XOR）满足结合律，并且对一个数进行两次完全相同的异或运算会得到原来的数，因此我们可以通过异或运算找到缺失的数字。
        int len = nums.length;
        for (int i = 0; i < nums.length; i++) {
            len ^= i ^ nums[i];
        }
        return len;
    }

    private static int missingNumber4(int[] nums) {
        int sum = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += i + 1 - nums[i];
        }
        return sum;
    }

}
