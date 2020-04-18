package morphia;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mawt
 * @description
 * @date 2020/4/17
 */
public class MorphiaTest {

    public static void main(String[] args) {
        Morphia morphia = new Morphia();
        morphia.mapPackage("com.wode.entity"); // entity所在包路径
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        Datastore datastore = morphia.createDatastore(mongoClient, "morphia");
    }

    private void add() {

        Morphia morphia = new Morphia();
        morphia.mapPackage("com.wode.entity"); // entity所在包路径
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        Datastore datastore = morphia.createDatastore(mongoClient, "morphia");



        //先添加@Reference引用的对象
        CommodityInfo cmdtyInfo = new CommodityInfo();
        cmdtyInfo.setColor("silver");
        cmdtyInfo.setStyle("12");
        datastore.save(cmdtyInfo);

        //再添加商品
        Commodity cmdty = new Commodity();
        cmdty.setCommodityInfo(cmdtyInfo);
        cmdty.setCmdtyCode("Ag");
        cmdty.setCmdtyName("银");

        List<Attribute> attributes = new ArrayList<>();
        Attribute attribute = new Attribute();
        attribute.setKey("品质");
        attribute.setValue("优");
        attributes.add(attribute);
        cmdty.setAttributes(attributes);

        Key<Commodity> saveKey = datastore.save(cmdty);

    }


}
