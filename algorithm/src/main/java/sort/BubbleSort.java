package sort;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class BubbleSort {

    private int[] arr = {2, 1, 3, 9, 1, 7, 0, 3, 5, 9};

    @Before
    public void printBefore() {
        System.out.println(Arrays.toString(arr));
    }

    @Test
    public void bubbleSort() {
        System.out.println("冒泡排序，两两比较，把最大的依次放在最后，时间复杂度O(N^2)，额外空间复杂度O(1)");

        int temp;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    @Test
    public void insertionSort() {
        System.out.println("插入排序，时间复杂度O(N^2)，额外空间复杂度O(1)");
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i + 1; j > 0; j--) {
                if (arr[j] < arr[j - 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j - 1];
                    arr[j - 1] = temp;
                }
            }
        }
    }

    @Test
    public void insertionSort2() {
        System.out.println("插入排序，时间复杂度O(N^2)，额外空间复杂度O(1)。");
        System.out.println(" 插入排序（Insertion Sort）：最佳情况：T(n) = O(n)   最坏情况：T(n) = O(n2)   平均情况：T(n) = O(n2)\n" +
                "        假定n是数组的长度，\n" +
                "        首先假设第一个元素被放置在正确的位置上，这样仅需从1-n-1范围内对剩余元素进行排序。对于每次遍历，从0-i-1范围内的元素已经被排好序，\n" +
                "        每次遍历的任务是：通过扫描前面已排序的子列表，将位置i处的元素定位到从0到i的子列表之内的正确的位置上。\n" +
                "        将arr[i]复制为一个名为target的临时元素。\n" +
                "        向下扫描列表，比较这个目标值target与arr[i-1]、arr[i-2]的大小，依次类推。\n" +
                "        这个比较过程在小于或等于目标值的第一个元素(arr[j])处停止，或者在列表开始处停止（j=0）。\n" +
                "        在arr[i]小于前面任何已排序元素时，后一个条件（j=0）为真，\n" +
                "        因此，这个元素会占用新排序子列表的第一个位置。\n" +
                "        在扫描期间，大于目标值target的每个元素都会向右滑动一个位置（arr[j]=arr[j-1]）。\n" +
                "        一旦确定了正确位置j，\n" +
                "        目标值target（即原始的arr[i]）就会被复制到这个位置。\n" +
                "        与选择排序不同的是，插入排序将数据向右滑动，并且不会执行交换。");
        int current;
        for (int i = 0; i < arr.length - 1; i++) {
            current = arr[i + 1];
            int preIndex = i;
            while (preIndex >= 0 && current < arr[preIndex]) {
                arr[preIndex + 1] = arr[preIndex];
                preIndex--;
            }
            arr[preIndex + 1] = current;
        }
    }

    @Test
    public void selectionSort() {
        System.out.println("选择排序，每次从现有数据挑出一个最小的直接放在最前面，第二次再将剩余的数据进行排序，拿出最小的放在最前面，以此类推");
        int minIndex, temp;
        for (int i = 0; i < arr.length - 1; i++) {
            minIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[minIndex]) { //找到最小的数
                    minIndex = j; //将最小数的索引保存
                }
            }
            if (minIndex != i) {
                temp = arr[minIndex];
                arr[minIndex] = arr[i];
                arr[i] = temp;
            }
            // 执行完一次循环，当前索引 i 处的值为最小值，直到循环结束即可完成排序
        }
    }

    @Test
    public void shellSort() {
        System.out.println("希尔排序，");
        int len = arr.length, current, gap = len >>> 1;
        int num = 0;
        while (gap > 0) {
            System.out.println(++num);
            for (int i = gap; i < len; i++) {
                current = arr[i];
                int preIndex = i - gap;
                while (preIndex >= 0 && arr[preIndex] > current) {
                    arr[preIndex + gap] = arr[preIndex];
                    preIndex -= gap;
                }
                arr[preIndex + gap] = current;
            }
            gap = gap >>> 1;
        }
    }

    @Test
    public void mergeSort() {
        System.out.println("归并排序，");
        MergeSort(arr);
    }

    public int[] MergeSort(int[] array) {
        if (array.length < 2) return array;
        int mid = array.length / 2;
        int[] left = Arrays.copyOfRange(array, 0, mid);
        int[] right = Arrays.copyOfRange(array, mid, array.length);
        return merge(MergeSort(left), MergeSort(right));
    }

    /**
     * 归并排序——将两段排序好的数组结合成一个排序数组
     *
     * @param left
     * @param right
     * @return
     */
    public int[] merge(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];
        for (int index = 0, i = 0, j = 0; index < result.length; index++) {
            if (i >= left.length)
                result[index] = right[j++];
            else if (j >= right.length)
                result[index] = left[i++];
            else if (left[i] > right[j])
                result[index] = right[j++];
            else
                result[index] = left[i++];
        }
        return result;
    }


    @Test
    public void quickSort() {
        System.out.println("快速排序，");

    }

    @Test
    public void heapSort() {
        System.out.println("堆排序，");

    }

    @Test
    public void countingSort() {
        System.out.println("计数排序，");

    }

    @Test
    public void bucketSort() {
        System.out.println("桶排序，");

    }

    @Test
    public void radixSort() {
        System.out.println("基数排序，");

    }

    @After
    public void print() {
        System.out.println(Arrays.toString(arr));
    }

}
