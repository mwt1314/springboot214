package cn.matio.apo.mongodb.multisource.config;

import cn.matio.apo.mongodb.multisource.config.MongoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Description: mongo配置类
 * @Author:杨攀
 * @Since:2019年7月8日上午11:19:39
 */
@ConfigurationProperties(prefix = "topinfo.mongodb.base")
@PropertySource(value = {"classpath:multi-source.yml"})
@Component
public class MongoBaseConfiguration extends MongoConfiguration {
 
    

}