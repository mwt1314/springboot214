package morphia2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MongoProperties {

    @Value("${mongo.uri:}")
    protected String uri;

    @Value("${mongo.dbName:}")
    protected String dbName;

    public String getUri() {
        return uri == null ? null : uri.trim();
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDbName() {
        return dbName == null ? null : dbName.trim();
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}