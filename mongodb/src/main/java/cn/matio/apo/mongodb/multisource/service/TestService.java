package cn.matio.apo.mongodb.multisource.service;

import cn.matio.apo.mongodb.multisource.bean.UserBean;

import java.util.List;

public interface TestService {
    void saveUser(UserBean user);

    void saveUserList(List<UserBean> userList);

    void updateUser(UserBean user);

    void deleteUserById(Long id);
}
