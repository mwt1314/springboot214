package leetcodecn.digui;

public class 零钱兑换 {

    public static void main(String[] args) {
        System.out.println("题目来源：https://leetcode-cn.com/problems/coin-change/");
        System.out.println("零钱兑换：给定不同面额的硬币 coins 和一个总金额 amount。" +
                "编写一个函数来计算可以凑成总金额所需的最少的硬币个数。" +
                "如果没有任何一种硬币组合能组成总金额，返回 -1。\n");

        int[] coins = {1, 2, 5};
        int amount = 11;
        System.out.println(coinChange(coins, amount));
    }

    public static int coinChange(int[] coins, int amount) {


        return -1;
    }

}
