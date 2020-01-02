package cn.matio.apo.mongodb.multisource.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "topinfo.mongodb.warn")
@PropertySource(value = {"classpath:multi-source.yml"})
@Component
public class MongoWarnConfiguration extends MongoConfiguration {
 
     
}