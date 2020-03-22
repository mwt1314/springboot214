package leetcodecn.digui;

public class 盛最多水的容器11 {

    public static void main(String[] args) {
        System.out.println("题目来源：https://leetcode-cn.com/problems/container-with-most-water/");
        System.out.println("11. 盛最多水的容器");
        System.out.println("给你 n 个非负整数 a1，a2，...，an，每个数代表坐标中的一个点 (i, ai) 。" +
                "在坐标内画 n 条垂直线，垂直线 i 的两个端点分别为 (i, ai) 和 (i, 0)。找出其中的两条线，" +
                "使得它们与 x 轴共同构成的容器可以容纳最多的水。\n" +
                "说明：你不能倾斜容器，且 n 的值至少为 2。");
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
