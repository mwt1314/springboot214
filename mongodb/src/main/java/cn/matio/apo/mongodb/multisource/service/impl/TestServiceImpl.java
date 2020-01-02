package cn.matio.apo.mongodb.multisource.service.impl;

import cn.matio.apo.mongodb.multisource.bean.UserBean;
import cn.matio.apo.mongodb.multisource.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    @Qualifier("baseMongoTemplate")
    private MongoTemplate mongoTemplate;

    @Override
    public void saveUser(UserBean user) {
    //    String userJson = JSON.toJSONString(user);
    //    mongoTemplate.save(userJson, "tx");
    }

    @Override
    public void saveUserList(List<UserBean> userList) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateUser(UserBean user) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteUserById(Long id) {
        // TODO Auto-generated method stub

    }

}