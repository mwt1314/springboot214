package foureleven;

import org.junit.Test;

public class Four2Eleven {

    /**
     * 取一个整数a从右端开始的4～7位
     * <p>
     * 　  (1)先使a右移4位。
     * 　　(2)设置一个低4位全为1,其余全为0的数。可用~(~0 < <4)
     * 　　(3)将上面二者进行&运算。
     */
    @Test
    public void substr() {
        int num = 12345689;
        //Math.floor(a);  返回不大于a的最大整数
        //Math.pow(a,b); 求a的b次方
        num = (int) Math.floor(num % Math.pow(10, 7) / Math.pow(10, 3));
        System.out.println(num);
    }

}
