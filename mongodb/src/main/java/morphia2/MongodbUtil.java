package morphia2;

import com.mongodb.DBObject;
import org.bson.types.ObjectId;

public class MongodbUtil {
    public static String getString(DBObject obj, String key) {
        Object val = obj.get(key);
        return val == null ? "" : val.toString();
    }

    public static Integer getInteger(DBObject obj, String key) {
        Object val = obj.get(key);
        if (val != null) {
            if (val instanceof Number) {
                return ((Number) val).intValue();
            }
            return Integer.parseInt(val.toString());
        }
        return null;
    }

    public static int getIntValue(DBObject obj, String key) {
        Integer v = MongodbUtil.getInteger(obj, key);
        return v==null?0:v.intValue();
    }
    public static Long getLong(DBObject obj, String key) {
        Object val = obj.get(key);
        if (val != null) {
            return Long.parseLong(val.toString());
        }
        return null;
    }
    public static long getLongValue(DBObject obj, String key) {
        Long v = MongodbUtil.getLong(obj, key);
        return v==null?0:v.longValue();
    }
    public static Double getDouble(DBObject obj, String key) {
        Object val = obj.get(key);
        return val == null ? null : (Double) val;
    }

    public static Boolean getBoolean(DBObject obj, String key) {
        Object val = obj.get(key);
        return val == null ? null : (Boolean) val;
    }

    public static String genMongoId() {
        return ObjectId.get().toString();
    }

}