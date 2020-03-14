package factor;

import org.junit.Test;

public class 贪心算法GreedyAlgorithmTest {

    @Test
    public void greedy() {
        System.out.println("已上传到CSDN");
        System.out.println("贪心算法，钱币找零问题");
        System.out.println("贪心算法：又称贪婪算法(Greedy Algorithm)，是指在对问题求解时，总是做出在当前看来是最好的选择。也就是说，不从整体最优解出发来考虑，它所做出的仅是在某种意义上的局部最优解。\n" +
                "    它是一种分阶段的工作，在每一个阶段，可以认为所做决定是最好的，而不考虑将来的后果。这种“眼下能够拿到的就拿”的策略是这类算法名称的来源\n" +
                "    贪心策略适用的前提是：局部最优策略能导致产生全局最优解。也就是当算法终止的时候，局部最优等于全局最优");

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
