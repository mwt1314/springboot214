package cn.matio.api.solr.service;

import cn.matio.api.solr.vo.User;

import java.util.List;

public interface SolrService {
    List<User> addUser();
}