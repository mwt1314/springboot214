package cn.matio.api.solr.controller;

import cn.matio.api.solr.service.SolrService;
import cn.matio.api.solr.vo.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
public class SolrController {

    @Autowired
    private SolrService solrService;

    @Autowired
    private SolrClient solrClient;

    //批量增加
    @RequestMapping("/addUsers")
    public void addUsers() throws IOException, SolrServerException {
        List<User> users = solrService.addUser();

        solrClient.addBeans(users);
        solrClient.commit();
    }

    //单个增加
    @RequestMapping("/addUser")
    public void addUser() throws IOException, SolrServerException {
        User user = new User();
        user.setId("456788");
        user.setName("王强");
        user.setAddress("北京市");
        user.setSex("女");
        user.setHost(456752);
        solrClient.addBean(user);
        solrClient.commit();
    }

    //根据id查询
    @RequestMapping("/getByIdFromSolr/{id}")
    public void getByIdFromSolr(@PathVariable("id") String id) throws IOException, SolrServerException {
        //根据id查询内容
        SolrDocument solrDocument = solrClient.getById(id);

        //方法1
        Gson gson = new Gson();
        String solrString = gson.toJson(solrDocument);
        User user = gson.fromJson(solrString, User.class);

        //方法2
        Map<String, Object> map = solrDocument.getFieldValueMap();
        user = gson.fromJson(map.toString(), User.class);


        //获取filedName
        Collection<String> fieldNames = solrDocument.getFieldNames();
        //获取file名和内容
        Map<String, Object> fieldValueMap = solrDocument.getFieldValueMap();

//            int childDocumentCount = solrDocument.getChildDocumentCount();

        List<SolrDocument> childDocuments = solrDocument.getChildDocuments();

        System.out.println("byId==================" + solrDocument);
        System.out.println("fieldNames==================" + fieldNames);
        System.out.println("fieldValueMap==================" + fieldValueMap);
//            System.out.println("childDocumentCount=================="+childDocumentCount);
        System.out.println("childDocuments==================" + childDocuments);

    }

    //根据di删除
    @RequestMapping("/delById/{id}")
    public void delById(@PathVariable("id") String id) throws IOException, SolrServerException {
        //根据id删除信息
        UpdateResponse updateResponse = solrClient.deleteById(id);
        //执行的时间
        long elapsedTime = updateResponse.getElapsedTime();

        int qTime = updateResponse.getQTime();
        //请求地址
        String requestUrl = updateResponse.getRequestUrl();
        //请求的结果{responseHeader={status=0,QTime=2}}
        NamedList<Object> response = updateResponse.getResponse();
        //请求结果的头{status=0,QTime=2}
        NamedList responseHeader = updateResponse.getResponseHeader();
        //请求的状态 0
        int status = updateResponse.getStatus();

        System.out.println("elapsedTime===========" + elapsedTime);
        System.out.println("qTime===========" + qTime);
        System.out.println("requestUrl===========" + requestUrl);
        System.out.println("response===========" + response);
        System.out.println("responseHeader===========" + responseHeader);
        System.out.println("status===========" + status);
    }

    @RequestMapping("/queryFromSolr")
    public Object queryFromSolr() throws IOException, SolrServerException {
        //第一种方式
//        Map<String, String> queryParamMap = new HashMap<String, String>();
//        queryParamMap.put("q", "*:*");
//        queryParamMap.put("f1","id,name");
//        queryParamMap.put("sort","id asc");
//        MapSolrParams mapSolrParams = new MapSolrParams(queryParamMap);
//        solrClient.query(mapSolrParams);

        //第二种方式
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("*:*");
//        solrQuery.addField("*");
        solrQuery.add("q", "id:4567");

        solrQuery.setSort("id", SolrQuery.ORDER.asc);
        //设置查询的条数
        solrQuery.setRows(50);
        //设置查询的开始
        solrQuery.setStart(0);
        //设置高亮
        solrQuery.setHighlight(true);
        //设置高亮的字段
        solrQuery.addHighlightField("item_name");
        //设置高亮的样式
        solrQuery.setHighlightSimplePre("<font color='red'>");
        solrQuery.setHighlightSimplePost("</font>");
        System.out.println(solrQuery);
        QueryResponse response = solrClient.query(solrQuery);
        //返回高亮显示结果
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
        //response.getResults();查询返回的结果
        SolrDocumentList documentList = response.getResults();
        for (SolrDocument solrDocument : documentList) {
            System.out.println("solrDocument==============" + solrDocument);
        }
        return documentList;
    }

    public List<User> queryAll() {
        List<User> list = null;
        SolrQuery query = new SolrQuery();
        query.setQuery("*:*");
        query.setStart(0);
        query.setRows(20);
        try {
            QueryResponse response = solrClient.query(query);
            SolrDocumentList documentList = response.getResults();
            if (!documentList.isEmpty()) {
                Gson gson = new Gson();
                String listString = gson.toJson(documentList);
                list = gson.fromJson(listString, new TypeToken<List<User>>() {
                }.getType());
//              list = convertToModel(documentList);
            } else {
                //    list = userMapper.queryAll();
                //    solrClient.addBeans(list);
            }

        } catch (SolrServerException e) {
          e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<User> queryByCondition(String de) {
        List<User> list = null;
        // 关键字模糊查询
        SolrQuery query = new SolrQuery();
        String nameLike = "userName:*" + de +  "*";
        String desLike = " OR description:*" + de+  "*";
        String sexLike = " OR sex:*" + de +  "*";
        String addLike = " OR address:*" + de +  "*";
        query.set("q",nameLike + desLike + sexLike + addLike);

        query.setStart(0);
        query.setRows(20);
        try {
            QueryResponse response = solrClient.query(query);
            SolrDocumentList documentList = response.getResults();
            if (!documentList.isEmpty()) {
                Gson gson = new Gson();
                String listString = gson.toJson(documentList);
                list = gson.fromJson(listString, new TypeToken<List<User>>() {}.getType());
            } else {
            }

        } catch (SolrServerException e) {
        } catch (IOException e) {
        }
        return list;
    }

}