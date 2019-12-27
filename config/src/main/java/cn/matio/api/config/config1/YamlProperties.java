package cn.matio.api.config.config1;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author mawt
 * @description
 * @date 2019/12/27
 */

/**
 * 一. @PropertySource + @Value
 * 配置参数类代码
 *
 * @PropertySource: 为了告知springboot加载自定义的yml配置文件，springboot默认会自动加载application.yml文件，如果参数信息直接写在这个文件里，则不需要显式加载。
 * @Value: 指定目标属性在yml’文件中的全限定名。
 * @Component: 作用是将当前类实例化到spring容器中，相当于xml配置文件中的<bean id="" class=""/>
 */
@Component
@PropertySource(value = {"classpath:application.yml"})
@Data
public class YamlProperties {

    @Value("${system.user.name}")
    private String name;

    @Value("${system.user.password}")
    private String password;

    @Value("${system.user.age}")
    private int age;

}
