package cn.matio.api.config.config2;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 二. @ConfigurationProperties + @PropertySource + @Value
 * 配置参数类代码：
 * 添加了@ConfigurationProperties注解，并且设置了prefix前缀属性的值，这样@Value只需要指定参数名称，省略了前缀路径。
 *
 *
 */
@Component
@PropertySource(value = {"classpath:test.yml"})
@ConfigurationProperties(prefix = "system.user1")
@Data
public class YamlProperties2 {

    @Value("${name}")
    private String name;
    @Value("${password}")
    private String password;
    @Value("${age}")
    private int age;

}