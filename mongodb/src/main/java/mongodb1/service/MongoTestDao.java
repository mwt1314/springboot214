package mongodb1.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.InsertManyOptions;
import mongodb1.bean.MongoTest;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Component
public class MongoTestDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 创建对象
     */
    public MongoTest insertOne(MongoTest test) {
        MongoTest save = mongoTemplate.save(test);
        return save;
    }

    // 生成长度为length的随机数
    private static long genRand(int length) {
        if (length == 0) {
            length = 16;
        }
        String allchar = "0123456789";
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            stringBuffer.append(allchar.charAt(random.nextInt(allchar.length())));
        }
        return Long.parseLong(stringBuffer.toString());
    }

    public void insertMany() {
        MongoCollection<Document> collection = mongoTemplate.createCollection("c1");

        String[] telephone = new String[]{""};
        String[] desc = new String[]{"", "", ""};
        long start = System.currentTimeMillis();
        int pl = telephone.length;
        int dl = desc.length;
        Random random = new Random();
        collection.createIndex(new Document("id", 1));
        collection.createIndex(new Document("tel", 1));
        List<Document> docList = new ArrayList<>();
        InsertManyOptions insertManyOptions = new InsertManyOptions().ordered(false);
        long total = 0;
        for (int i = 0; i < 100; i++) {
            Date date = new Date();
            for (int j = 0; j < 1000; j++) {
                Document doc = new Document("id", genRand(16)).append("tel", telephone[random.nextInt(pl)])
                        .append("desc", desc[random.nextInt(dl)]).append("number", genRand(random.nextInt(10)))
                        .append("importtime", date);
                docList.add(doc);
                total++;
            }
            collection.insertMany(docList, insertManyOptions);
            docList.clear();
        }
        long end = System.currentTimeMillis();
        System.out.println("插入" + total + "条数据耗时" + (end - start) / 1000.0 + "秒");
    }

    /**
     * 根据用户名查询对象
     *
     * @return
     */
    public MongoTest findTestByName(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        MongoTest mgt = mongoTemplate.findOne(query, MongoTest.class);
        return mgt;
    }

    /**
     * 更新对象
     */
    public void updateTest(MongoTest test) {
        Query query = new Query(Criteria.where("id").is(test.getId()));
        Update update = new Update().set("age", test.getAge()).set("name", test.getName());

        //更新查询返回结果集的第一条
        mongoTemplate.updateFirst(query, update, MongoTest.class);
        //更新查询返回结果集的所有
        // mongoTemplate.updateMulti(query,update,TestEntity.class);
    }

    /**
     * 删除对象
     *
     * @param id
     */
    public void deleteTestById(Integer id) {
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, MongoTest.class);
    }

}