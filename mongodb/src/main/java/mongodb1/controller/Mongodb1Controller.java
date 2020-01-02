package mongodb1.controller;

import com.mongodb.client.model.InsertManyOptions;
import mongodb1.bean.MongoTest;
import mongodb1.service.MongoTestDao;
import cn.matio.api.mongodb.UserAttachmentRel;
import cn.matio.api.mongodb.UserAttachmentRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Random;

@RestController
public class Mongodb1Controller {

    @Autowired
    private MongoTestDao mtdao;

    /**
     *
     * 默认集合名称为className
     * id相同时会覆盖
     *
     *  db.mongoTest.find()
     * { "_id" : 11, "age" : 33, "name" : "ceshi", "_class" : "mongodb1.bean.MongoTest" }
     * { "_id" : 1, "age" : 25, "name" : "matio", "_class" : "mongodb1.bean.MongoTest" }
     * { "_id" : 2, "age" : 25, "name" : "matio", "_class" : "mongodb1.bean.MongoTest" }
     * { "_id" : 233, "age" : 25, "name" : "matio", "_class" : "mongodb1.bean.MongoTest" }
     * @param id
     * @param name
     * @param age
     * @throws Exception
     */
    @GetMapping(value = "/insertOne")
    public void insertOne(int id, String name, int age) throws Exception {
        MongoTest mgtest = new MongoTest();
        mgtest.setId(id);
        mgtest.setAge(age);
        mgtest.setName(name);
        mtdao.insertOne(mgtest);
    }

    @GetMapping(value = "/insertMany")
    public void insertMany() throws Exception {
        mtdao.insertMany();
    }

    @GetMapping(value = "/findByName")
    public MongoTest findTestByName() {
        MongoTest mgtest = mtdao.findTestByName("ceshi");
        System.out.println("mgtest is " + mgtest);
        return mgtest;
    }

    @GetMapping(value = "/test3")
    public void updateTest() {
        MongoTest mgtest = new MongoTest();
        mgtest.setId(11);
        mgtest.setAge(44);
        mgtest.setName("ceshi2");
        mtdao.updateTest(mgtest);
    }

    @GetMapping(value = "/test4")
    public void deleteTestById() {
        mtdao.deleteTestById(11);
    }

}
