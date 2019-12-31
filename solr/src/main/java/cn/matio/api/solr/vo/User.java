package cn.matio.api.solr.vo;

import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;

@Data
public class User implements Serializable {

    @Field("id") //使用这个注释，里面的名字是根据你在solr数据库中配置的来决定
    private String id;
    @Field("item_name")
    private String name;
    @Field("item_sex")
    private String sex;
    @Field("item_address")
    private String address;
    @Field("item_host")
    private Integer host;

}