package cn.matio.api.config.config3;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 三. YamlPropertiesFactoryBean + @Value
 *  去掉了@PropertySource 注解，改为定义一个PropertySourcesPlaceholderConfigurer类型的@Bean来加载配置文件信息。
 */
@Component
@Data
public class YamlProperties3 {
    
    @Value("${system.user3.name}")
    private String name;

    @Value("${system.user3.password}")
    private String password;

    @Value("${system.user3.age}")
    private int age;

}