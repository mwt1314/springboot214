package leetcodecn.digui;

public class 盛最多水的容器11 {

    public static void main(String[] args) {

        System.out.println(maxArea(new int[]{2, 3, 4, 5, 18, 17, 6}));
    }

    public static int maxArea(int[] height) {
        int len = height.length, mid = len >>> 1;
        int square = 0;
        for (int x = 0; x < len; x++) {
            for (int y = x + 1; y < len; y++) {
                square = Math.max(square, (y - x) * Math.min(height[x], height[y]));
            }
        }
        return square;
    }

    public int maxArea2(int[] height) {
        int maxarea = 0, left = 0, right = height.length - 1;
        while (left < right) {
            maxarea = Math.max(maxarea, Math.min(height[left], height[right]) * (right - left));
            if (height[left] < height[right])
                left++;
            else
                right--;
        }
        return maxarea;
    }

}
