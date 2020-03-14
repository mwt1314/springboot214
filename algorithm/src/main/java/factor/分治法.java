package factor;

import org.junit.Test;

public class 分治法 {

    private int[] arr = {-2, 1, -3, 4, -1, 2, 1, -5, 4};

    @Test
    public void test() {
        System.out.println("给定一个序列（至少含有 1 个数），从该序列中寻找一个连续的子序列，使得子序列的和最大。 \n" +
                "例如，给定序列 [-2,1,-3,4,-1,2,1,-5,4]， \n" +
                "连续子序列 [4,-1,2,1] 的和最大，为 6。 \n" +
                "扩展练习: \n" +
                "若你已实现复杂度为 O(n) 的解法，尝试使用更为精妙的分治法求解。");
        System.out.println(maxSubArray1(arr));
        System.out.println(maxSubArray2(arr));
        System.out.println(maxSubArray3(arr));
    }

    private int maXSubArray(int[] nums) {
        int current = nums[0];
        int max = 0;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] < 0) {

            } else {

            }

        }
        return max;
    }

    private int maxSubArray1(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];
        dp[0] = nums[0];
        int max = dp[0];

        for (int i = 1; i < n; i++) {
            dp[i] = Math.max(nums[i] + dp[i - 1], nums[i]);
            max = Math.max(max, dp[i]);

        }
        return max;
    }

    private int maxSubArray2(int[] nums) {
        int length = nums.length;
        int sum = nums[0];
        int tmpnum = 0;
        for (int num : nums) {
            tmpnum += num;
            if (tmpnum > sum) {
                sum = tmpnum;
            }
            if (tmpnum <= 0) {
                tmpnum = 0;
            }

        }
        return sum;
    }

    private int maxSubArray3(int[] nums) {
        int sum = 0, max = Integer.MIN_VALUE;
        for (int num : nums) {
            sum = sum < 0 ? num : (sum + num);
            max = Math.max(sum, max);
        }
        return max;
    }


}
