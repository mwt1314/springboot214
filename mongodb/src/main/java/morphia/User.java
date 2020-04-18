package morphia;

import lombok.Data;
import org.mongodb.morphia.annotations.*;

import java.util.List;

/**
 * @author mawt
 * @description
 * @date 2020/4/17
 */
@Data
@Entity(value = "user")
public class User {

    @Id
    private String id;

    @Property("name")
    private String name;

    private int age;

    @Indexed
    private String phone;

    @Transient
    private String extra;

    public List<String> hobbies;

}
