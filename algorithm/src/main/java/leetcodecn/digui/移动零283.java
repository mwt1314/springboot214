package leetcodecn.digui;

public class 移动零283 {

    public static void main(String[] args) {

    }

    public void moveZeroes(int[] nums) {
        int len = nums.length;
        for (int i = 0; i < len; i++) {
            if (nums[i] == 0) {
                //发现为0的元素，找到后面第一个非0的元素，移动位置
                int j = i + 1;
                while (j < len && nums[j] == 0) {
                    j++;
                }
                if (j < len) {
                    int temp = nums[i];
                    nums[i] = nums[j];
                    nums[j] = temp;
                }
            }
        }
    }

    public void moveZeroes2(int[] nums) {
        int len = nums.length, nonZeroIndex = 0;
        for (int i = 0; i < len; i++) {
            if (nums[i] != 0) {
                nums[nonZeroIndex++] = nums[i];
                //避免原地覆盖
                if (i != nonZeroIndex) {
                    nums[i] = 0;
                }
            }
        }
    }

    public void moveZeroes3(int[] nums) {
        int lastNonZeroIndex = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                //如果不是一个位置，index赋值nums[i],nums[i] =0
                if (i != lastNonZeroIndex) {
                    nums[lastNonZeroIndex] = nums[i];
                    nums[i] = 0;
                }
                //位置向后移动
                lastNonZeroIndex++;
            }
        }
    }

}
