package cn.matio.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author mawt
 * @description
 * @date 2019/12/27
 */
@SpringBootApplication
@EnableScheduling
public class ScheduleMain {

    public static void main(String[] args) {
        //新增额外的配置文件
        System.setProperty(ConfigFileApplicationListener.CONFIG_ADDITIONAL_LOCATION_PROPERTY, "classpath:/external/");
        //新增的额外的配置文件的名称
        System.setProperty(ConfigFileApplicationListener.CONFIG_NAME_PROPERTY, "application,schedule");

        SpringApplication.run(ScheduleMain.class, args);
    }

}
