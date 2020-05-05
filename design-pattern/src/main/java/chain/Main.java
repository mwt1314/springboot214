import chain.ChainOfResponsibilityClient;
import chain.CustomInterceptor;
import chain.Request;
import chain.Result;

/**
 * 类描述：责任链模式测试类
 *
 * @author lzy
 *
 */
public class Main {

    public static void main(String[] args) {

        Request request = new Request.Builder().setName("张三").setDays(5)
                .setReason("事假").build();
        ChainOfResponsibilityClient client = new ChainOfResponsibilityClient();
        client.addRatifys(new CustomInterceptor());
        Result result = client.execute(request);

        System.out.println("结果：" + result.toString());
    }

}