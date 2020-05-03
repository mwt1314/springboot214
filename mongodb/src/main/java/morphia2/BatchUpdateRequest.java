package morphia2;

import com.mongodb.DBObject;

public class BatchUpdateRequest {

    private Object pk;

    private DBObject filter;

    private DBObject update;

    private DBObject inc;

    public DBObject getFilter() {
        return filter;
    }

    public void setFilter(DBObject filter) {
        this.filter = filter;
    }

    public DBObject getUpdate() {
        return update;
    }

    public void setUpdate(DBObject update) {
        this.update = update;
    }

    public Object getPk() {
        return pk;
    }

    public void setPk(Object pk) {
        this.pk = pk;
    }

    public DBObject getInc() {
        return inc;
    }

    public void setInc(DBObject inc) {
        this.inc = inc;
    }
}