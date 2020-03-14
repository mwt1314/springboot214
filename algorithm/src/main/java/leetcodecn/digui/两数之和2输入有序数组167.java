package leetcodecn.digui;

public class 两数之和2输入有序数组167 {

    public static void main(String[] args) {

    }

    public int[] twoSum(int[] numbers, int target) {
        int len;
        if (numbers == null || (len = numbers.length) < 2) return null;
        int sum;
        for (int start = 0, end = len - 1; start < end;) {
            if ((sum = numbers[start] + numbers[end]) == target) {
                return new int[]{start + 1, end + 1};
            } else if (sum < target) {
                start++;
            } else {
                end--;
            }
        }
        return null;
    }
}
