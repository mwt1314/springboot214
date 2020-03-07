package greedyalgorithm;

import org.junit.Test;

public class GreedyAlgorithmTest {

    @Test
    public void greedy() {
        System.out.println("贪心算法，钱币找零问题");

        int[] prices = {100, 50, 20, 10, 5, 1};
        int money = 123, temp = money;
        for (int i = 0; i < prices.length; i++) {
            if (temp > prices[i]) {
                System.out.println("需要" + temp / prices[i] + "张" + prices[i] + "元");
                temp = temp % prices[i];
                if (temp == 0) {
                    break;
                }
            }
        }

    }

}
