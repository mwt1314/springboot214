package morphia2;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.converters.BigDecimalConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PreDestroy;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class MongoAutoConfiguration {
    protected Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MongoProperties mongoProperties;

    @Bean(name = "dsForRW")
    public Datastore dsForRW() throws Exception {
        MongoClient mongoClient = createInstance(mongoProperties);
        Datastore dsForRWMdt = morphia().createDatastore(mongoClient, mongoProperties.getDbName());
        afterCreateDs(dsForRWMdt);
        return dsForRWMdt;
    }

    @PreDestroy
    public void close(){
        log.info("mongoClient is destroy");
    }

    private void afterCreateDs(Datastore dsForRW)  throws UnknownHostException {
        try {
            dsForRW.ensureIndexes();
            dsForRW.ensureCaps();
//          BasicDBObject keys = new BasicDBObject();
//          keys.put("link", 1);
//          keys.put("shortLink", 1);

//          DBCollection dbCollection = ds.getCollection(ShortLink.class);
//          dbCollection.createIndex(keys);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private MongoClient createInstance(MongoProperties mongoProperties)  throws UnknownHostException {

        MongoClientOptions.Builder builder = MongoClientOptions.builder();
        builder.socketKeepAlive(true);
        builder.connectTimeout(5000);
        builder.socketTimeout(0);
//      builder.maxWaitTime(60000);
        builder.heartbeatFrequency(2000);// 心跳频率

        MongoClientURI clientURI = new MongoClientURI(mongoProperties.getUri(),builder);
        MongoClient mongoClient = new MongoClient(clientURI);
        if(StringUtils.isEmpty(mongoProperties.getDbName())){
            mongoProperties.setDbName(clientURI.getDatabase());
        }
        return mongoClient;
    }

    private Morphia morphia() {
        //
        Morphia morphia = new Morphia();
        // 手动加载
        if (0 == morphia.getMapper().getMappedClasses().size()) {
//          morphia.map(com.dachen.shorturl.domains.ShortLink.class);
        }
        Set<Class> clazzSet = this.getAllEntity("com.dachen");
        if(clazzSet!=null && clazzSet.size()>0){
            morphia.map(clazzSet);
        }
        morphia.getMapper().getConverters().addConverter(BigDecimalConverter.class);
        return morphia;
    }

    @SuppressWarnings("rawtypes")
    private Set<Class> getAllEntity(String packageName){
        List<Class<?>> returnClassList = ClassUtil.getClasses(packageName) ;
        if(returnClassList==null || returnClassList.isEmpty()){
            return null;
        }
        Set<Class>clazzSet = new HashSet<>();
        for(Class<?> clazz:returnClassList){
            Entity entity = clazz.getAnnotation(Entity.class);
            if(entity!=null){
                log.info("loading mongo entity:{},table:{}",clazz.getName(),entity.value());
                clazzSet.add(clazz);
            }
        }
        return clazzSet;
    }

}