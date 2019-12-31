package cn.matio.api.mongodb;

import lombok.Data;

import java.util.Date;

@Document(collection="user")
@Data
public class User {

    private String userId;

    private String name;

    private String uclass;

    private String email;

    private Date birthday;

    private int age;

    private int dataStatus;

}