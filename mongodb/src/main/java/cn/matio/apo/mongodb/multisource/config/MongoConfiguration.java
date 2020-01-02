package cn.matio.apo.mongodb.multisource.config;

import lombok.Data;

/**
 * mongo配置类 - 父类
 *
 */
@Data
public class MongoConfiguration {

    private String uri;

    private String database;
}