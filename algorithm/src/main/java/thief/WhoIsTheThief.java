package thief;

/**
 * 谁是窃贼：
 * 公安人员审问四名窃贼嫌疑犯。已知，这四人当中仅有一名是窃贼，还知道这四人中每人要么是诚实的，要么总是说谎。在回答公安人员的问题中：
 * 甲说：“乙没有偷，是丁偷的。”
 * 乙说：“我没有偷，是丙偷的。”
 * 丙说：“甲没有偷，是乙偷的。”
 * 丁说：“我没有偷”
 * 请根据这四人的谈话判断谁是盗窃者
 */
public class WhoIsTheThief {

    public static void main(String[] args) {
        for (int i = 0; i < 4; i++) {
            // 第一次循环赋甲为贼  第二次赋乙为贼...
            boolean[] isthief = {false, false, false, false};
            isthief[i] = true;
            //题目说每个人的话要么全为真   要么全为假
            //所以有两种可能   所以只要符合一句话全为真或全为假就可以
            //所以一句话表示为全为真  和  全为假两种形式   用或连接   足以符合题目要求
            //最后还得符合四句话   所以 将他们用户&&连接   必须符合题目的四个条件
            if (((!isthief[1] && isthief[3]) || (isthief[1] && !isthief[3])) && // 甲说：“乙没有偷，是丁偷的。”
                    ((!isthief[1] && isthief[2]) || (isthief[1] && !isthief[2])) && // 乙说：“我没有偷，是丙偷的。”
                    ((!isthief[0] && isthief[1]) || (isthief[0] && !isthief[1])) && // 丙说：“甲没有偷，是乙偷的。”
                    (!isthief[3] || isthief[3])) { // 丁说：“我没有偷”
                switch (i) {
                    case 0:
                        System.out.println("甲是贼");
                        break;
                    case 1:
                        System.out.println("乙是贼");
                        break;
                    case 2:
                        System.out.println("丙是贼");
                        break;
                    case 3:
                        System.out.println("丁是贼");
                        break;
                    default:
                        break;
                }
            }
        }
    }


}
