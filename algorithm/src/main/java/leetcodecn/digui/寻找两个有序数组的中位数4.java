package leetcodecn.digui;

import java.util.Arrays;

public class 寻找两个有序数组的中位数4 {

    public static void main(String[] args) {
        System.out.println("4. 寻找两个有序数组的中位数");
        System.out.println("给定两个大小为 m 和 n 的有序数组 nums1 和 nums2。\n" +
                "请你找出这两个有序数组的中位数，并且要求算法的时间复杂度为 O(log(m + n))。\n" +
                "你可以假设 nums1 和 nums2 不会同时为空。\n" +
                "\n" +
                "示例 1:\n" +
                "nums1 = [1, 3]\n" +
                "nums2 = [2]\n" +
                "则中位数是 2.0\n" +
                "示例 2:\n" +
                "nums1 = [1, 2]\n" +
                "nums2 = [3, 4]\n" +
                "则中位数是 (2 + 3)/2 = 2.5\n");


    }

    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int len1 = nums1.length, len2 = nums2.length;
        int len = len1 + len2;
        int[] newNums = new int[len];
        System.arraycopy(nums1, 0, newNums, 0, len1);
        System.arraycopy(nums2, 0, newNums, len1, len2);
        Arrays.sort(newNums);
        if ((len & 1) == 0) {
            return (newNums[len >>> 1] + newNums[(len >>> 1) - 1]) / 2.0;
        } else {
            return newNums[len >>> 1];
        }
    }

    public double findMedianSortedArrays2(int[] nums1, int[] nums2) {
        System.out.println("如果数组长度为奇数，；为偶数，");

        int len1 = nums1.length, len2 = nums2.length;
        int len = len1 + len2;
        int i = 0, j = 0;
        while (i < len1 && j < len2) {

        }
        return 0.0d;
    }

}
