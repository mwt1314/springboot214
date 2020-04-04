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

    public int maxSubArray2(int[] nums) {
        //wrong answer
        int max = 0, len = nums.length, now = nums[0], temp;
        for (int i = 1; i < len; i++) {
            temp = Math.max(nums[i] + now, nums[i]);
            max = Math.max(temp, now);
            now = temp;
        }
        return max;
    }

}
