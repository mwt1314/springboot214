package binary;

public class Find {

    public static void main(String[] args) {
        System.out.println("二分查找，针对有序数组的");
        int[] binaryNums = {1, 6, 15, 18, 27, 50};
        int findValue = 27;
        System.out.println(find(binaryNums, 0, binaryNums.length - 1, findValue));
    }

    private static int find(int[] arr, int low, int high, int target) {
        if (low <= high) {
            int mid = (low + high) >>> 1;
            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] > target) {
                return find(arr, 0, mid - 1, target);
            } else {
                return find(arr, mid, high - 1, target);
            }
        }
        return -1;
    }

}
