package cn.matio.api.schedule3;

import lombok.Data;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

/**
 * @author mawt
 * @description
 * @date 2019/12/11
 */
@Component
@Data
public class OrderJobThread implements SchedulingConfigurer {

    private String name = "测试"; //调用set方法可以动态设置日志名

    private String cron = "* 0/10 * * * ?"; //调用set方法可动态设置时间规则

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addCronTask(new Runnable() {

            @Override
            public void run() {
                System.out.println(name + " --- > 开始");
                //业务逻辑
                System.out.println(name + " --- > 结束");
            }
        }, cron);
    }

}
