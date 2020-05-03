package morphia2;

import com.mongodb.*;
import morphia2.vo.AbstractBaseInfo;
import org.bson.types.ObjectId;
import org.mongodb.morphia.AdvancedDatastore;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class MongoCommonDAO {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource(name = "dsForRW")
    protected AdvancedDatastore dsForRW;


    public <T> T getByObjectId(final Class<?> clazz, ObjectId id){
        @SuppressWarnings("unchecked")
        Query<T> query = (Query<T>) dsForRW.createQuery(clazz);
        query.filter(Mapper.ID_KEY,id);
        return query.get();
    }

    public <T> T getByPK(final Class<?> clazz,String pk){
        Query <T> query = this.createQuery(clazz, pk);
        return query.get();
    }

    public <T> T findAndModifyByPK(final Class<?> clazz, String pk, DBObject update){
        Query <T> query = this.createQuery(clazz, pk);
        UpdateOperations<T> ops = this.createUpdateOperations(clazz, update);
        return dsForRW.findAndModify(query, ops);
    }

    public <T> T findAndModify(final Class<?> clazz,DBObject filter,DBObject update){
        Query<T> query = this.createQuery(clazz, filter);
        UpdateOperations<T> ops = this.createUpdateOperations(clazz,update);
        return dsForRW.findAndModify(query, ops);
    }

    public <T> T findAndModifyOrCreate(final Class<?> clazz,DBObject filter,DBObject update){
        Query<T> query = this.createQuery(clazz, filter);
        UpdateOperations<T> ops = this.createUpdateOperations(clazz,update);
        return dsForRW.findAndModify(query, ops,false, true);
    }

    public <T> UpdateResults update(final Class<?> clazz, DBObject filter, DBObject update)
    {
        Query<T> query = this.createQuery(clazz, filter);
        UpdateOperations<T> ops = this.createUpdateOperations(clazz, update);
        return dsForRW.update(query, ops);
    }

    @SuppressWarnings("unchecked")
    public <T> UpdateResults addToArray(final Class<?> clazz, DBObject filter, String fieldExpr, List<?> values, boolean addDups)
    {
        Query<T> query = this.createQuery(clazz, filter);
        UpdateOperations<T> ops = (UpdateOperations<T>) dsForRW.createUpdateOperations(clazz);
        ops.addAll(fieldExpr, values,addDups);
        return dsForRW.update(query, ops);
    }

    @SuppressWarnings("unchecked")
    public <T> UpdateResults removeFromArray(final Class<?> clazz,DBObject filter,String fieldExpr, Object value)
    {
        Query<T> query = this.createQuery(clazz, filter);
        UpdateOperations<T> ops = (UpdateOperations<T>) dsForRW.createUpdateOperations(clazz);
        ops.removeAll(fieldExpr, value);
        return dsForRW.update(query, ops);
    }

    @SuppressWarnings("unchecked")
    public <T> UpdateResults removeAllFromArray(final Class<?> clazz,DBObject filter,String fieldExpr, List<?> values){
        Query<T> query = this.createQuery(clazz, filter);
        UpdateOperations<T> ops = (UpdateOperations<T>) dsForRW.createUpdateOperations(clazz);
        ops.removeAll(fieldExpr, values);
        return dsForRW.update(query, ops);
    }


    public <T>T insertOrUpdate(T entity)
    {
        if (entity == null) {
            return null;
        }
        if (entity instanceof AbstractBaseInfo) {
            AbstractBaseInfo obj = (AbstractBaseInfo) entity;
            if (obj.getId() == null) {
                obj.setId(MongodbUtil.genMongoId());
                dsForRW.insert(obj);
                return entity;
            }
        }
        dsForRW.save(entity);
        return entity;
    }
//  public <T>T insert(T entity) {
//      if (entity == null) {
//          return null;
//      }
//      Key<T> key = dsForRW.insert(entity);
//      return entity;
//  }
    public void batchInsert(List<?> objList) {
        Object[]arrays = new Object[objList.size()];
        int i=0;
        for(Object obj :objList){
            if(obj instanceof AbstractBaseInfo){
                AbstractBaseInfo entity = (AbstractBaseInfo)obj;
                if(entity.getId()==null)
                {
                    entity.setId(MongodbUtil.genMongoId());
                }
                arrays[i++]=entity;
            }else{
                arrays[i++]=obj;
            }
        }
        dsForRW.insert(arrays);
    }

    public <T> T get(final Class<?> clazz,String field,Object value)
    {
        Query<T> query = this.createQuery(clazz, new BasicDBObject(field,value));
        return query.get();
    }

    public <T> T get(final Class<?> clazz,DBObject filter)
    {
        Query<T> query = this.createQuery(clazz, filter);
        return query.get();
    }

    public <T> List<T> getList(final Class<?> clazz,String field,Object value){
        return this.getList(clazz, new BasicDBObject(field,value));
    }

    public <T> List<T> getList(final Class<?> clazz,DBObject filter)
    {
        Query<T> query = this.createQuery(clazz, filter);
        return query.asList();
    }

    public <T> List<T> getList(final Class<?> clazz,DBObject filter,String sort)
    {
        Query<T> query = this.createQuery(clazz, filter);
        if(sort != null){
            query.order(sort);
        }
        return query.asList();
    }

    public <T> List<T> getList(final Class<?> clazz,DBObject filter,String sort,int limit)
    {
        Query<T> query = this.createQuery(clazz, filter);
        if(sort != null){
            query.order(sort);
        }
        if(limit>0){
            query.offset(0).limit(limit);
        }
        return query.asList();
    }
//  /**
//   * 
//   * @param clazz
//   * @param filter 过滤条件
//   * @param sort 排序条件 <li>{@code order("age,-date")} (age ascending, date descending)</li>
//   * @param pageSize  每页大小
//   * @param pageIndex 第几页，从0开始
//   * @return
//   */
//  public <T> PageVO<T> getPageData(final Class<?> clazz,DBObject filter,String sort,int pageSize,int pageIndex){
//      if(pageSize<=0){
//          pageSize = 20;
//      }
//      Query<T> query = this.createQuery(clazz, filter);
//      long totalCount = query.countAll();
//      if(StringUtils.isNotEmpty(sort)){
//          query.order(sort);
//      }
//      query.offset(pageIndex * pageSize).limit(pageSize);
//      List<T> list= query.asList();
//      PageVO<T> result = new PageVO<>(list,totalCount,pageSize);
//      return result;
//  }

    public boolean exists(final Class<?> clazz,DBObject query){
        return dsForRW.getCollection(clazz).getCount(query)>0;
    }

    public <T> void remove(final Class<?> clazz,DBObject filter){
        if(filter==null){
            throw new ServiceException("删除条件为空");
        }
        Set<String> key = filter.keySet();
        if(key==null || key.isEmpty()){
            throw new ServiceException("删除条件为空");
        }
        Query<T> query = this.createQuery(clazz, filter);
        dsForRW.delete(query);
    }

    @SuppressWarnings("unchecked")
    public <T> void cleanTable(final Class<?> clazz){
        Query<T> query = (Query<T>) dsForRW.createQuery(clazz);
        dsForRW.delete(query);
    }
    /**
     * field支持多级嵌套 ，比如：order.patient.id
     */
    public List<String> stringFieldList(final Class<?> clazz,DBObject filter,String field){

        DBObject projection = new BasicDBObject();
        projection.put(field, 1);//1表示返回的数据 只包含这个字段，0表示排除

        DBCursor cursor =  dsForRW.getCollection(clazz).find(filter, projection);
        List<String> fieldValueList = new ArrayList<String>();
        while (cursor.hasNext()) {
            DBObject obj = cursor.next();
            fieldValueList.add(getFieldValue(obj,field.split("\\.")));
        }
        return fieldValueList;
    }

    private String getFieldValue(DBObject dbObj,String[]fields){
         int length = fields.length;
         if(length>1){
             Object obj = dbObj.get(fields[0]);
             if(obj instanceof DBObject){
                String[]subFields = new String[length-1];
                System.arraycopy(fields, 1, subFields, 0, length-1);
                return getFieldValue((DBObject)obj,subFields);
             }
         }else if(length==1){
             return MongodbUtil.getString(dbObj, fields[0]);
         }
         return null;
    }


    @SuppressWarnings("unchecked")
    private <T> UpdateOperations<T> createUpdateOperations(final Class<?> clazz,DBObject update){
        UpdateOperations<T> ops = (UpdateOperations<T>) dsForRW.createUpdateOperations(clazz);
        for( String field:update.keySet()){
            ops.set(field, update.get(field));
        }
        return ops;
    }

    @SuppressWarnings("unchecked")
    private <T> Query<T> createQuery(final Class<?> clazz,DBObject filter) {
        Query<T> query = (Query<T>) dsForRW.createQuery(clazz);
        if(filter!=null){
            for(String condition:filter.keySet()){
                query.filter(condition, filter.get(condition));
            }
        }
        return query;
    }


    @SuppressWarnings("unchecked")
    private <T> Query<T> createQuery(final Class<?> clazz,String id) {
        Query<T> query = (Query<T>) dsForRW.createQuery(clazz);
        query.filter(Mapper.ID_KEY,id);
        return query;
    }
    /**
     * @see org.mongodb.morphia.query.FieldEndImpl
     * 模糊查询 :左右模糊   TODO
     * 
     *   DBObject query = new BasicDBObject();
         query.put("name", like(userName));
     */
    private BasicDBObject like(String content) {
         Pattern pattern = Pattern.compile("^.*" + content + ".*$", Pattern.CASE_INSENSITIVE);
         return new BasicDBObject("$regex", pattern);
    }

    /*
     * 模糊查询: TODO
        DBObject query = new BasicDBObject();
        query.put("filename", endWith(“.pdf”));
     */
    private BasicDBObject endWith(String content) {
        Pattern pattern = Pattern.compile(content + "$", Pattern.MULTILINE);
        return new BasicDBObject("$regex", pattern);
    }

    public <T> DBCollection getCollection(Class<?> clazz) {
        return dsForRW.getCollection(clazz);
    }
    /**
     * 排除某些属性
     */
    public <T> T getInfoExcludeField(Class<?> clazz, DBObject filter, String... ignoringFields) {
        Query<T> query = this.createQuery(clazz, filter);
        if(ignoringFields!=null && ignoringFields.length>0){
            query.retrievedFields(false, ignoringFields);
        }
        return query.get();
    }

    public <T> T getInfoIncludeField(Class<?> clazz, DBObject filter,String... includeFields) {
        Query<T> query = this.createQuery(clazz, filter);
        if(includeFields!=null && includeFields.length>0){
            query.retrievedFields(true, includeFields);
        }
        return query.get();
    }

    public <T> List<T> getListExcludeField(Class<?> clazz, DBObject filter,String sort, String... ignoringFields) {
        Query<T> query = this.createQuery(clazz, filter);
        if(ignoringFields!=null && ignoringFields.length>0){
            query.retrievedFields(false, ignoringFields);
        }
        if(sort != null){
            query.order(sort);
        }
        return query.asList();
    }

    public <T> List<T> getListIncludeField(Class<?> clazz, DBObject filter,String sort, String... includeFields) {
        Query<T> query = this.createQuery(clazz, filter);
        if(sort != null){
            query.order(sort);
        }
        if(includeFields!=null && includeFields.length>0){
            query.retrievedFields(true, includeFields);
        }
        return query.asList();
    }

    /**
     * {
        key: { userId: 1 },
        cond: { appId:  "10001" },
        reduce: function( curr, result ) {
                    result.total += curr.money;
                },
        initial: { total : 0 }
       }
       ==
       select userId,sum(money) as total from tablename where appId="10001"
     * <p>描述：</p>
     * @param clazz
     * @param key
     * @param cond
     * @param initial
     * @param reduce
     */
    @SuppressWarnings("unchecked")
    public List<DBObject> group(final Class<?> clazz,final DBObject key, final DBObject cond, final DBObject initial, final String reduce){
        return (List<DBObject>) dsForRW.getCollection(clazz).group(key, cond, initial, reduce);
    }

    /**
     * select userId,sum(money) as total from tablename where appId="10001"
     * 条件(pipeline0)   :{$match:{"appId":"10001"}}
     * 分组统计(pipeline1):{$group:{"_id":"$userId","total":{$sum:"$money"}}}
     *  group支持操作：$sum,$avg,$first,$last,$max,$min,$push,$addToSet,$stdDevPop,$stdDevSamp
     * <p>描述：</p>
     * @param clazz
     * @param
     * @return
     */
//  private Iterable<DBObject> aggregate(final Class<?> clazz,final List<? extends DBObject> pipeline){
//      AggregationOutput out = dsForRW.getCollection(clazz).aggregate(pipeline);
//      if(out!=null){
//          return out.results();
//      }
////        dsForRW.getCollection(clazz).aggregate(pipeline, options)
//      return null;
//  }

    @SuppressWarnings("unchecked")
    public List<Object> distinct(final Class<?> clazz,final String fieldName, final DBObject query){
        return dsForRW.getCollection(clazz).distinct(fieldName, query);
    }

    public long count(final Class<?> clazz,final DBObject query){
        return dsForRW.getCollection(clazz).count(query);
    }
    //***************************************************************************//
    /**
     * 
     * @param clazz
     * @param requestList
     * @param updateOne true表示只更新匹配的一条数据
     * @param createIfMissing 如果不存在则插入新的文档[查询部分和更新部分的结合 ]
     */
    public void batchUpdate(final Class<?> clazz,List<BatchUpdateRequest> requestList,boolean updateOne,boolean createIfMissing) {
        if(requestList==null || requestList.size()<=0){
            return;
        }
        int PAGE_SIZE =1000;
        int totalSize = requestList.size();
        if(totalSize<=PAGE_SIZE){
            this.batchUpdateByPage(clazz, requestList,updateOne,createIfMissing);
        }else{
            int lastPageCount = totalSize % PAGE_SIZE;
            int count = totalSize / PAGE_SIZE;
            if(lastPageCount>0) {
                count++;
            }
            List<BatchUpdateRequest>subList = null;
            int toIndex = 0;
            for(int i=0;i<count;i++)
            {
                toIndex =(i+1) * PAGE_SIZE; 
                if(toIndex > totalSize){
                    toIndex = totalSize;
                }
                subList = requestList.subList(i * PAGE_SIZE, toIndex);
                this.batchUpdateByPage(clazz, subList,updateOne,createIfMissing);
            }
        }

    }

    private void batchUpdateByPage(final Class<?> clazz,List<BatchUpdateRequest> requestList,boolean updateOne,boolean createIfMissing) {
        BulkWriteOperation bulk = dsForRW.getCollection(clazz).initializeOrderedBulkOperation();
        for(BatchUpdateRequest request : requestList){
            if(request.getFilter()==null && request.getPk()!=null){
                request.setFilter(new BasicDBObject("_id", request.getPk()));
            }
            DBObject update = new BasicDBObject();
            if(request.getUpdate()!=null){
                update.put("$set", request.getUpdate());
            }
            if(request.getInc()!=null){
                update.put("$inc", request.getInc());
            }
            if(update.keySet()==null || update.keySet().isEmpty()){
                continue;
            }
            if(createIfMissing){
                if(updateOne){
                    bulk.find(request.getFilter()).upsert().updateOne(update);
                }else{
                    bulk.find(request.getFilter()).upsert().update(update);
                }
            }else{
                if(updateOne){
                    bulk.find(request.getFilter()).updateOne(update);
                }else{
                    bulk.find(request.getFilter()).update(update);
                }
            }
        }
        bulk.execute();
    }
    /**
     * 暂不支持嵌套对象
     * @param clazz
     * @param objList
     */
    public void batchInsertOrUpdate(final Class<?> clazz,List<? extends AbstractBaseInfo> objList) {
        if(objList==null || objList.size()<=0){
            return;
        }
        BulkWriteOperation bulk = dsForRW.getCollection(clazz).initializeOrderedBulkOperation();

        DBObject document;
        for(AbstractBaseInfo obj :objList){
            if(obj.getId()==null){
                obj.setId(MongodbUtil.genMongoId());
            }
            try {
                document = bean2DBObject(obj);
                bulk.find(new BasicDBObject("_id", obj.getId())).upsert().updateOne(new BasicDBObject("$set",document));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                logger.error(e.getMessage());
            }
        }
        bulk.execute();
    }

    /**
     * 暂不支持嵌套对象
     * @param clazz
     * @param objList
     */
    public void batchUpdate(final Class<?> clazz,List<? extends AbstractBaseInfo> objList) {
        BulkWriteOperation bulk = dsForRW.getCollection(clazz).initializeUnorderedBulkOperation();
        for(AbstractBaseInfo obj :objList){
            if(obj.getId()==null){
                continue;
            }
            DBObject document;
            try {
                document = bean2DBObject(obj);
                bulk.find(new BasicDBObject("_id", obj.getId())).updateOne(new BasicDBObject("$set",document));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                logger.error(e.getMessage());
            }
        }
        bulk.execute();
    }
//  public MongoClient mongoClient(){
//      return dsForRW.getMongo();
//  }

    private static <T> DBObject bean2DBObject(T bean) throws IllegalArgumentException, IllegalAccessException {
        if (bean == null) {
            return null;
        }
        DBObject dbObject = new BasicDBObject();
        // 获取对象对应类中的所有属性域
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 获取属性名
            String varName = field.getName();
            // 修改访问控制权限
            boolean accessFlag = field.isAccessible();
            if (!accessFlag) {
                field.setAccessible(true);
            }
            Object param = field.get(bean);
            if (param == null) {
                continue;
            } 
            dbObject.put(varName, param);
            // 恢复访问控制权限
            field.setAccessible(accessFlag);
        }
        return dbObject;
    }
}