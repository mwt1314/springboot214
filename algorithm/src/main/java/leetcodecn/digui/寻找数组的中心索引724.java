package leetcodecn.digui;

import java.util.Arrays;

public class 寻找数组的中心索引724 {

    public static void main(String[] args) {
        寻找数组的中心索引724 x = new 寻找数组的中心索引724();
        System.out.println(x.pivotIndex(new int[]{-1, -1, -1, 0, 1, 1}));
    }

    public int pivotIndex(int[] nums) {
        String x = "";
        char[] chars = x.toCharArray();
        Arrays.sort(chars);

        int len;
        if (nums == null || (len = nums.length) < 3) return -1;

        int total = 0, left = 0;
        for (int num : nums) {
            total += num;
        }
        for (int i = 0; i < len; i++) {
            if ((2 * left + nums[i]) == total) {
                return i;
            }
            left += nums[i];
        }
        return -1;
    }

}
