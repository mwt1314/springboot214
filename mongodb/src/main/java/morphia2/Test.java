package morphia2;

import com.mongodb.MongoClient;
import com.mongodb.client.model.InsertManyOptions;
import morphia2.vo.MUser;
import org.junit.Before;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.InsertOptions;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Test {

    private Datastore datastore;

    @Before
    public void before() {
        Morphia morphia = new Morphia();
        morphia.mapPackage("morphia2.vo");
        datastore = morphia.createDatastore(new MongoClient("192.168.0.133"), "morphia_db");
    }

    @org.junit.Test
    public void insert() {
        List<MUser> mUserList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            MUser mUser = new MUser();
            mUser.setUsername("mwt" + i);
            mUser.setAge(random.nextInt(20));
            mUser.setSalary(random.nextDouble() * 100000.0d);
            mUser.setPassword("12345");
            mUserList.add(mUser);
        }
        Key<Iterator<MUser>> save = datastore.save(mUserList.iterator());
        Object id = save.getId();
        String collection = save.getCollection();
        Class<? extends Iterator<MUser>> type = save.getType();
        System.out.println("id=" + id);
        System.out.println("col=" + collection);
        System.out.println(type);
    }

    @org.junit.Test
    public void query() {
        Query<MUser> query = datastore.createQuery(MUser.class);

        query.filter("age <=", 30);

        List<MUser> mUserList = query.asList();
        mUserList.forEach(System.out::println);
    }


}
