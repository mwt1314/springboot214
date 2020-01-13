//package java;
//
//import cn.matio.api.elasticsearch.repository.ItemRepository;
//import cn.matio.api.elasticsearch.vo.Item;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author mawt
// * @description
// * @date 2019/12/31
// */
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = EsDemoApplication.class)
//public class ESTest {
//
//    @Autowired
//    private ElasticsearchTemplate elasticsearchTemplate;
//
//    /**
//     * @Description:创建索引，会根据Item类的@Document注解信息来创建
//     * @Author: https://blog.csdn.net/chen_2890
//     * @Date: 2018/9/29 0:51
//     */
//    @Test
//    public void testCreateIndex() {
//        elasticsearchTemplate.createIndex(Item.class);
//    }
//    /**
//     * @Description:删除索引
//     * @Author: https://blog.csdn.net/chen_2890
//     * @Date: 2018/9/29 0:50
//     */
//    @Test
//    public void testDeleteIndex() {
//        elasticsearchTemplate.deleteIndex(Item.class);
//    }
//
//    @Autowired
//    private ItemRepository itemRepository;
//
//    /**
//     * @Description:定义新增方法
//     * @Author: https://blog.csdn.net/chen_2890
//     */
//    @Test
//    public void insert() {
//        Item item = new Item(1L, "小米手机7", " 手机",
//                "小米", 3499.00, "http://image.baidu.com/13123.jpg");
//        itemRepository.save(item);
//    }
//
//    @Test
//    public void insertList() {
//        List<Item> list = new ArrayList<>();
//        list.add(new Item(2L, "坚果手机R1", " 手机", "锤子", 3699.00, "http://image.baidu.com/13123.jpg"));
//        list.add(new Item(3L, "华为META10", " 手机", "华为", 4499.00, "http://image.baidu.com/13123.jpg"));
//        // 接收对象集合，实现批量新增
//        itemRepository.saveAll(list);
//    }
//    /**
//     * @Description:定义修改方法
//     * @Author: https://blog.csdn.net/chen_2890
//     */
//    @Test
//    public void update(){
//        Item item = new Item(1L, "苹果XSMax", " 手机",
//                "小米", 3499.00, "http://image.baidu.com/13123.jpg");
//        itemRepository.save(item);
//    }
//    /**
//     * @Description:定义查询方法,含对价格的降序、升序查询
//     * @Author: https://blog.csdn.net/chen_2890
//     */
//    @Test
//    public void testQueryAll(){
//        // 查找所有
//        //Iterable<Item> list = this.itemRepository.findAll();
//        // 对某字段排序查找所有 Sort.by("price").descending() 降序
//        // Sort.by("price").ascending():升序
//        Iterable<Item> list = this.itemRepository.findAll(Sort.by("price").ascending());
//
//        for (Item item:list){
//            System.out.println(item);
//        }
//    }
//
//}
