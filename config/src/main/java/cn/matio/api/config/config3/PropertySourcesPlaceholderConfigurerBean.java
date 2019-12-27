package cn.matio.api.config.config3;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class PropertySourcesPlaceholderConfigurerBean {

    @Bean
    public PropertySourcesPlaceholderConfigurer yaml() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        /**
         * 自定义读取yml配置文件
         */
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("config3.yml"));
        configurer.setProperties(yaml.getObject());
        return configurer;
    }

}