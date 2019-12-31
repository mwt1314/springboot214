package cn.matio.api.elasticsearch.repository;

import cn.matio.api.elasticsearch.vo.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ItemRepository extends ElasticsearchRepository<Item,Long> {

}