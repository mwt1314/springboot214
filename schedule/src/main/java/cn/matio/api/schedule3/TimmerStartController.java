package cn.matio.api.schedule3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/*
有时希望项目在启动的时候加载一些系统参数或者方法，就要用到ApplicationRunner
ApplicationRunner是一个接口，我们需要实现它，并重写run()方法，当项目启动时，run()方法便会自动执行
 */
@Component
@Order(value = 1)  //value值会 从小至大的执行
public class TimmerStartController implements ApplicationRunner {

    @Autowired
    private OrderJobThread orderJobThread;  //得到定时任务

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("=========== 项目启动后，初始化 定时任务执行时间 =============");
        orderJobThread.setCron("0/5 * * * * ?");  //根据需求重新赋值时间规则
        orderJobThread.setName("ordersTasks"); //赋值name
    }

}