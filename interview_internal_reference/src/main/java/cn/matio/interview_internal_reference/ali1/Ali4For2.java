package cn.matio.interview_internal_reference.ali1;

import java.util.HashMap;

/**
 * @author mawt
 * @description
 * @date 2020/1/2
 */
public class Ali4For2 {

    public static class LRUCache {

        private int capacity;
        private LRUNode head;
        private LRUNode tail;
        private HashMap<String, LRUNode> map;

        public LRUCache() {
            this.capacity = 3;
            this.map = new HashMap<>();
        }

        public <T> void put(String key, T t) {
            LRUNode node;
            if (map.containsKey(key)) {
                node = map.get(key);
                node.val = t;
                //移除node
                removeNode(node);
            } else {
                node = new LRUNode(key, t);
                if (map.size() >= capacity) {
                    removeNode(node);
                }
            }
            setHead(node);
            map.put(key, node);
        }

        public <T> T get(String key) {
            LRUNode<T> node = map.get(key);
            if (node != null) {
                //将get的元素放到head
                removeNode(node);
                setHead(node);
                return node.val;
            }
            return null;
        }

        //将node节点设置为head节点
        private void setHead(LRUNode node) {
            if (node != null) {
                //将node放在头部
                if (head == null) {
                    tail = node;
                } else {
                    node.next = head;
                    head.prev = node;
                }
                head = node;
            }
        }

        //将节点移除
        private void removeNode(LRUNode node) {
            if (node != null) {
                if (node.next != null) {
                    node.next.prev = node.prev;
                } else {
                    tail = node.prev;
                }
                if (node.prev != null) {
                    node.prev.next = node.next;
                } else {
                    head = node.next;
                }
                map.remove(node.key);
            }
        }

    }

    public static class LRUNode<T> {

        String key;
        T val;
        LRUNode prev;
        LRUNode next;

        public LRUNode(String key, T val) {
            this.key = key;
            this.val = val;
        }
    }

    public static void main(String[] args) {

    }

}
