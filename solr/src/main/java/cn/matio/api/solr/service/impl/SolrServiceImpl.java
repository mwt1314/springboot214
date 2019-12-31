package cn.matio.api.solr.service.impl;

import cn.matio.api.solr.service.SolrService;
import cn.matio.api.solr.vo.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SolrServiceImpl implements SolrService {

    @Override
    public List<User> addUser() {
        List<User> list = new ArrayList<>();
        User user = new User();
        for (int i = 0; i <5 ; i++) {
            user.setId(UUID.randomUUID().toString().replace("-",""));
            user.setName("jack"+i);
            if( i % 2 ==0) {
                user.setSex("男");
            }else {
                user.setSex("女");
            }
            user.setAddress("兰州市安宁区666"+i);
            user.setHost(73040+i);
            list.add(user);
        }
        return list;
    }

}