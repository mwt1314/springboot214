package leetcodecn.digui;

import java.util.Arrays;

public class 旋转数组189 {

    public static void main(String[] args) {
        int[] nums1 = new int[]{1,2,3,4,5,6,7};
        int k1 = 3;
        rotate(nums1, k1);
        System.out.println("nums=" + Arrays.toString(nums1));
    }

    public static void rotate(int[] nums, int k) {
        if (nums == null || nums.length < 2 || k < 1) return;
        int len = nums.length;
        k %= len;
        int[] x = new int[k];
        System.arraycopy(nums, len - k, x, 0, k);
        System.out.println("x=" + Arrays.toString(x));
        System.arraycopy(nums, 0, nums, k, len - k);
        System.out.println("nums=" + Arrays.toString(nums));
        System.arraycopy(x, 0, nums, 0, k);
    }

}
