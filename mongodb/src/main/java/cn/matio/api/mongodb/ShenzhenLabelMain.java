package cn.matio.api.mongodb;

import org.bson.BsonValue;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class ShenzhenLabelMain {

    private static MongoClient client = new MongoClient(new ServerAddress("192.168.218.133", 27017));
    // 数据库或集合不存在都会创建
    private static MongoCollection<Document> collection = client.getDatabase("sz_label").getCollection("sz_label_collection");

    public static void main(String[] args) {
        //	insert();
        //	update();
        //	find();
        //	mapreduce();
        aggregate();
    }

    private static void find() {
        long start = System.currentTimeMillis();
        MongoCursor<Document> iterator = collection.find(new Document("tel", new Document("$regex", Pattern.compile("^86")))).iterator();
        List<String> resultList = new ArrayList<String>();
        while (iterator.hasNext()) {
            Document document = iterator.next();
            resultList.add(document.toJson());
        }
        long end = System.currentTimeMillis();
        System.out.println("查询" + resultList.size() + "条，耗时:" + (end - start) / 1000.0 + "秒");
        client.close();
    }

    private static void mapreduce() {
        long start = System.currentTimeMillis();
        String map = "function() {emit(this.tel,{number:this.number});}";
        String reduce = "function(key,values) {var number=0;values.forEach(function(value) {number+=value.number});return {number:number}}";
        MapReduceIterable<Document> mapReduce = collection.mapReduce(map, reduce);
        MongoCursor<Document> iterator = mapReduce.iterator();
        List<Document> docList = new ArrayList<>();
        while (iterator.hasNext()) {
            Document document = iterator.next();
            System.out.println(document.toJson());
            docList.add(document);
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时:" + (end - start) / 1000.0 + "秒");
        client.close();
    }

    private static void aggregate() {
        long start = System.currentTimeMillis();
        List<Document> pipelines = new ArrayList<Document>();
        // select tel,sum(number) as total from sz_label.sz_label_collection where importtime >= '2018-11-08' and  importtime <= '2018-11-08' group by tel sort by total asc
        //	Document where = new Document("$match", new Document("$or", Arrays.asList(new Document("importtime", new Document("$gte", new Date())), new Document("importtime", new Document("$gte", new Date())))));
        Document group = new Document("$group", new Document("_id", "$tel").append("total", new Document("$sum", "$number")));
        Document sort = new Document("$sort", new Document("_id", 1));
        //	pipelines.add(where);
        pipelines.add(group);
        pipelines.add(sort);
        AggregateIterable<Document> aggregate = collection.aggregate(pipelines);
        MongoCursor<Document> iterator = aggregate.iterator();
        List<Document> docList = new ArrayList<>();
        while (iterator.hasNext()) {
            Document document = iterator.next();
            System.out.println(document.toJson());
            docList.add(document);
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时:" + (end - start) / 1000.0 + "秒");
        client.close();
    }

    private static void update() {
        long start = System.currentTimeMillis();
        UpdateResult updateResult = collection.updateMany(new Document("tel", "0719-6625410"), new Document("$inc", new Document("itemssize", 100)));
        long matchedCount = updateResult.getMatchedCount();
        long modifiedCount = updateResult.getModifiedCount();
        BsonValue upsertedId = updateResult.getUpsertedId();
        boolean modifiedCountAvailable = updateResult.isModifiedCountAvailable();
        //	UpdateResult acknowledged = UpdateResult.acknowledged(matchedCount, modifiedCount, upsertedId);
        //	UpdateResult unacknowledged = UpdateResult.unacknowledged();
        long end = System.currentTimeMillis();
        System.out.println("匹配" + matchedCount + "条，修改了 " + modifiedCount + "条，耗时:" + (end - start) / 1000.0 + "秒");
        client.close();
    }

    private static void insert() {
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
        client.close();
        long end = System.currentTimeMillis();
        System.out.println("插入" + total + "条数据耗时" + (end - start) / 1000.0 + "秒");
    }

    // 生成长度为length的随机数
    private static long genRand(int length) {
        if (length == 0)
            length = 16;
        String allchar = "0123456789";
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            stringBuffer.append(allchar.charAt(random.nextInt(allchar.length())));
        }
        return Long.parseLong(stringBuffer.toString());
    }

    private static String[] telephone = new String[]{""};
    private static String[] desc = new String[]{"", "", ""};
}