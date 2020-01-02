package cn.matio.interview_internal_reference.ali1;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 01.阿里篇/1.1.4 LRU缓存机制.md
 * https://github.com/0voice/interview_internal_reference/blob/master/01.%E9%98%BF%E9%87%8C%E7%AF%87/1.1.4%20LRU%E7%BC%93%E5%AD%98%E6%9C%BA%E5%88%B6.md
 * <p>
 * 题目：
 * LRU 缓存机制 设计和实现一个 LRU（最近最少使用Least Recently Used）缓存数据结构，使它应该支持一下操作：get 和 put。
 * get(key) - 如果 key 存在于缓存中，则获取 key 的 value（总是正数），否则返回 -1。
 * put(key,value) - 如果 key 不存在，请设置或插入 value。
 * 当缓存达到其容量时，它应该在插入新项目之前使最近最少使用的项目作废
 *
 * 知识点：
 * 1.LRU概念：Least Recently Used最近最少使用原则，一种常见的页面置换算法
 *      对于LRU的抽象总结如下：
 *          1.内存的容量是有限的
 *          2.当内存容量不足以存放需要内存的新数据时，必须丢掉最不常用的内存数据
 *
 *
 * 思路：
 *  问题在于我们如何找到最久没有用的缓存：
 *  一个最容易想到的办法是我们在给这个缓存数据加一个时间戳，每次get缓存时就更新时间戳，这样找到最久没有用的缓存数据问题就能够解决，但与之而来的会有两个新问题：
 *  虽然使用时间戳可以找到最久没用的数据，但我们最少的代价也要将这些缓存数据遍历一遍，除非我们维持一个按照时间戳排好序的SortedList。
 *  添加时间戳的方式为我们的数据带来了麻烦，我们并不太好在缓存数据中添加时间戳的标识，这可能需要引入新的包含时间戳的包装对象。
 *  而且我们的需要只是找到最久没用使用的缓存数据，并不需要精确的时间。添加时间戳的方式显然没有利用这一特性，这就使得这个办法从逻辑上来讲可能不是最好的
 *
 *
 */
public class Ali4 {

    public static void main(String[] args) {
        LRUCache lruCache = new LRUCache();
        lruCache.put("k1", 1);
        lruCache.put("k2", 2);
        lruCache.put("k3", 3);
        lruCache.put("k4", 4);
    //    lruCache.print();
        lruCache.put("k2", 3);
        lruCache.put("k5", 4);
        lruCache.print();
    }

    public static class LRUCache {

        int capacity = 3; //容量

        AtomicInteger num;

        Map<String, Integer> map;

        Map<String, Integer> lruMap;

        public LRUCache() {
            num = new AtomicInteger(0);
            map = new HashMap<>();
            lruMap = new HashMap<>();
        }

        public int get(String key) {
            if (map.containsKey(key)) {
                lruMap.put(key, num.incrementAndGet());
                return map.get(key);
            }
            return -1;
        }

        public void put(String key, Integer value) {
            if (map.size() < capacity) {
                map.put(key, value);
                lruMap.put(key, num.incrementAndGet());
            } else {
                //需要置换了
                //找到最久没有用的缓存
                List<Map.Entry<String, Integer>> list = new ArrayList<>(lruMap.entrySet());
                Collections.sort(list, Comparator.comparing(Map.Entry::getValue));
                String removeKey = list.get(0).getKey();
                lruMap.remove(removeKey);
                map.remove(removeKey);
            }
        }

        public void print() {
            map.forEach((k, v) -> {
                System.out.println(k + "-" + v);
            });
        }

    }

}
