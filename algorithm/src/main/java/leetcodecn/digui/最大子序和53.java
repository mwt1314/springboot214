package leetcodecn.digui;

public class 最大子序和53 {

    public static void main(String[] args) {


    }

    public int maxSubArray(int[] nums) {
        int sum = nums[0], len = nums.length, now = sum;
        for (int i = 1; i < len; i++) {
            now = Math.max(nums[i], now + nums[i]);
            sum = Math.max(sum, now);
        }
        return sum;
    }

}
