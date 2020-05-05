package arraylist;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 一般将数组中的每一个元素称作桶（segment），桶中连的链表或者红黑树中的每一个元素成为bin
 * <p>
 * 底层实现：数组 + 单向链表 + 红黑树
 * <p>
 * 红黑树定义和性质
 * 红黑树是一种含有红黑结点并能自平衡的二叉查找树。它必须满足下面性质：
 * <p>
 * 性质1：每个节点要么是黑色，要么是红色。
 * 性质2：根节点是黑色。
 * 性质3：每个叶子节点（NIL）是黑色。
 * 性质4：每个红色结点的两个子结点一定都是黑色。
 * 性质5：任意一结点到每个叶子结点的路径都包含数量相同的黑结点。
 * <p>
 * 从性质5又可以推出：
 * 性质5.1：如果一个结点A存在黑子结点，那么该结点A肯定有两个子结点
 * <p>
 * 红黑树自平衡的三种操作：左旋、右旋和变色
 * 左旋：以某个结点作为支点(旋转结点)，其右子结点变为旋转结点的父结点，右子结点的左子结点变为旋转结点的右子结点，左子结点保持不变。
 * 右旋：以某个结点作为支点(旋转结点)，其左子结点变为旋转结点的父结点，左子结点的右子结点变为旋转结点的左子结点，右子结点保持不变。
 * 变色：结点的颜色由红变黑或由黑变红。
 *
 * @param <K>
 * @param <V>
 */
public class HashMap源码分析<K, V> {

    //默认初始化容量 16。容量必须为2的次方。默认的hashmap大小为16
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

    static final int MAXIMUM_CAPACITY = 1 << 30;

    //默认加载因子
    //即实际数量超过总数DEFAULT_LOAD_FACTOR的数量即会发生resize动作
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    //树化阈值 8。当单个segment的容量超过阈值时，将链表转化为红黑树
    static final int TREEIFY_THRESHOLD = 8;

    //链表化阈值 6。当resize后或者删除操作后单个segment的容量低于阈值时，将红黑树转化为链表
    static final int UNTREEIFY_THRESHOLD = 6;

    //最小树化容量 64。当桶中的bin被树化时最小的hash表容量，低于该容量时不会树化
    static final int MIN_TREEIFY_CAPACITY = 64;

    //底层维护了一个table存放Node节点
    transient Node<K, V>[] table;

    transient Set<Map.Entry<K, V>> entrySet;

    transient int size;

    transient int modCount; //fast-fail

    //装载因子：用来衡量HashMap满的程度。loadFactor的默认值为0.75f。
    //计算HashMap的实时装载因子的方法为：size/capacity，而不是占用桶的数量去除以capacity
    final float loadFactor;

    //扩容阈值：当HashMap的size值大于threshold时会执行resize操作
    int threshold;

    public HashMap源码分析() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
    }

    public HashMap源码分析(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public HashMap源码分析(int initialCapacity, float loadFactor) {
        this.loadFactor = loadFactor;
        this.threshold = tableSizeFor(initialCapacity);
    }

    //单向链表
    static class Node<K, V> implements Map.Entry<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final String toString() {
            return key + "=" + value;
        }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public final boolean equals(Object o) {
            if (o == this)
                return true;
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
                if (Objects.equals(key, e.getKey()) &&
                        Objects.equals(value, e.getValue()))
                    return true;
            }
            return false;
        }
    }

    static class Entry<K, V> extends HashMap源码分析.Node<K, V> {
        Entry<K, V> before, after;

        Entry(int hash, K key, V value, Node<K, V> next) {
            super(hash, key, value, next);
        }
    }

    static final class TreeNode<K, V> extends Entry<K, V> {
        TreeNode<K, V> parent;  // red-black tree links
        TreeNode<K, V> left;
        TreeNode<K, V> right;
        TreeNode<K, V> prev;    // needed to unlink next upon deletion
        boolean red;

        TreeNode(int hash, K key, V val, Node<K, V> next) {
            super(hash, key, val, next);
        }

        final TreeNode<K, V> root() {    //找到根节点
            for (TreeNode<K, V> r = this, p; ; ) {
                if ((p = r.parent) == null)
                    return r;
                r = p;
            }
        }

        //这个方法是TreeNode中的方法，this为双向链表的头结点
        final void treeify(Node<K, V>[] tab) {
            TreeNode<K, V> root = null;  //红黑树的根节点
            for (TreeNode<K, V> x = this, next; x != null; x = next) {
                next = (TreeNode<K, V>) x.next;
                x.left = x.right = null;
                if (root == null) {
                    x.parent = null;
                    x.red = false;  //红黑树的根节点一定是黑色的
                    root = x;
                } else {
                    K k = x.key;
                    int h = x.hash;
                    Class<?> kc = null;
                    for (TreeNode<K, V> p = root; ; ) {
                        int dir, ph;   //判断
                        K pk = p.key;
                        //根据hash值判断是左子树还是右子树
                        if ((ph = p.hash) > h)
                            dir = -1;
                        else if (ph < h)
                            dir = 1;
                        else if ((kc == null &&
                                (kc = comparableClassFor(k)) == null) ||
                                (dir = compareComparables(kc, k, pk)) == 0)
                            dir = tieBreakOrder(k, pk);

                        TreeNode<K, V> xp = p;
                        if ((p = (dir <= 0) ? p.left : p.right) == null) {
                            x.parent = xp;
                            if (dir <= 0)   //成为左子树
                                xp.left = x;
                            else
                                xp.right = x;
                            root = balanceInsertion(root, x);   //红黑树自平衡：上色+左旋/右旋
                            break;
                        }
                    }
                }
            }
            moveRootToFront(tab, root);
        }

        /**
         *
         * @param root
         * @param x
         * @param <K>
         * @param <V>
         * @return 根节点
         */
        static <K, V> TreeNode<K, V> balanceInsertion(TreeNode<K, V> root, TreeNode<K, V> x) {
            x.red = true;   //插入结点是红色。理由：红色在父结点（如果存在）为黑色结点时，
            //红黑树的黑色平衡没被破坏，不需要做自平衡操作。但如果插入结点是黑色，
            //那么插入位置所在的子树黑色结点总是多1，必须做自平衡。(所有插入操作都是在叶子结点进行的，叶子节点一定是黑色)
            //xp：当前节点x的父节点
            //xpp：当前节点x的爷爷节点
            //xppl：当前节点x的爷爷节点的左子节点
            //xppr：当前节点x的爷爷节点的右子节点
            for (TreeNode<K, V> xp, xpp, xppl, xppr; ; ) {
                if ((xp = x.parent) == null) {  //如果当前节点没有父节点，代表x就是根节点，根节点为黑色
                    x.red = false;
                    return x;
                } else if (!xp.red || (xpp = xp.parent) == null)
                    //情况1：父节点xp为黑色，当前节点x为红色。因此红黑树已经自平衡，直接返回就可以了
                    //情况2：父节点xp为红色，根节点xp为黑色，当前节点x为红色，已经自平衡了（满足5大特性）
                    return root;

                //此时当前节点x为红色，父节点xp为红色，祖父节点xpp一定是黑色
                if (xp == (xppl = xpp.left)) {//如果父节点是祖父节点的左子节点xppl
                    if ((xppr = xpp.right) != null && xppr.red) {   //如果存在叔叔节点（右），并且还是红色
                        xppr.red = false;
                        xp.red = false;
                        xpp.red = true;
                        x = xpp;
                    } else {    //如果不存在叔叔节点  或者叔叔节点还是黑色
                        if (x == xp.right) {
                            root = rotateLeft(root, x = xp);    //左旋父节点xp，
                            xpp = (xp = x.parent) == null ? null : xp.parent;
                        }
                        if (xp != null) {
                            xp.red = false;
                            if (xpp != null) {
                                xpp.red = true;
                                root = rotateRight(root, xpp);
                            }
                        }
                    }
                } else { //如果xp是xpp的右子节点
                    if (xppl != null && xppl.red) {
                        xppl.red = false;
                        xp.red = false;
                        xpp.red = true;
                        x = xpp;
                    } else {
                        if (x == xp.left) {
                            root = rotateRight(root, x = xp);
                            xpp = (xp = x.parent) == null ? null : xp.parent;
                        }
                        if (xp != null) {
                            xp.red = false;
                            if (xpp != null) {
                                xpp.red = true;
                                root = rotateLeft(root, xpp);
                            }
                        }
                    }
                }
            }
        }

        /**
         * @param map   当前hashmap对象
         * @param tab   底层table
         * @param index 当前红黑树在table中的索引
         * @param bit   当前table的length
         */
        final void split(HashMap源码分析<K, V> map, Node<K, V>[] tab, int index, int bit) {
            TreeNode<K, V> b = this;    //当前table中某位置处的红黑树
            // Relink into lo and hi lists, preserving order
            TreeNode<K, V> loHead = null, loTail = null;
            TreeNode<K, V> hiHead = null, hiTail = null;
            int lc = 0, hc = 0;
            for (TreeNode<K, V> e = b, next; e != null; e = next) {
                next = (TreeNode<K, V>) e.next;
                e.next = null;
                if ((e.hash & bit) == 0) {  //扩容后位置不会改变
                    if ((e.prev = loTail) == null)
                        loHead = e;
                    else
                        loTail.next = e;
                    loTail = e;
                    ++lc;
                } else {  //扩容后位置发生改变
                    if ((e.prev = hiTail) == null)
                        hiHead = e;
                    else
                        hiTail.next = e;
                    hiTail = e;
                    ++hc;
                }
            }

            if (loHead != null) {
                if (lc <= UNTREEIFY_THRESHOLD)
                    tab[index] = loHead.untreeify(map);
                else {
                    tab[index] = loHead;
                    if (hiHead != null) // (else is already treeified)
                        loHead.treeify(tab);
                }
            }
            if (hiHead != null) {
                if (hc <= UNTREEIFY_THRESHOLD)
                    tab[index + bit] = hiHead.untreeify(map);
                else {
                    tab[index + bit] = hiHead;
                    if (loHead != null)
                        hiHead.treeify(tab);
                }
            }
        }

        final Node<K, V> untreeify(HashMap源码分析<K, V> map) {   //红黑树转回单向链表
            Node<K, V> hd = null, tl = null;
            for (Node<K, V> q = this; q != null; q = q.next) {
                Node<K, V> p = map.replacementNode(q, null);
                if (tl == null)
                    hd = p;
                else
                    tl.next = p;
                tl = p;
            }
            return hd;
        }

        /**
         * @param h hash(key)
         * @param k key
         * @return
         */
        final TreeNode<K, V> getTreeNode(int h, Object k) {
            return ((parent != null) ? root() : this).find(h, k, null);
        }

        final TreeNode<K, V> find(int h, Object k, Class<?> kc) {
            TreeNode<K, V> p = this;
            do {
                int ph, dir;
                K pk;
                TreeNode<K, V> pl = p.left, pr = p.right, q;
                if ((ph = p.hash) > h)
                    p = pl;
                else if (ph < h)
                    p = pr;
                else if ((pk = p.key) == k || (k != null && k.equals(pk)))
                    return p;
                else if (pl == null)
                    p = pr;
                else if (pr == null)
                    p = pl;
                else if ((kc != null ||
                        (kc = comparableClassFor(k)) != null) &&
                        (dir = compareComparables(kc, k, pk)) != 0)
                    p = (dir < 0) ? pl : pr;
                else if ((q = pr.find(h, k, kc)) != null)
                    return q;
                else
                    p = pl;
            } while (p != null);
            return null;
        }

        static <K,V> TreeNode<K,V> rotateLeft(TreeNode<K,V> root, TreeNode<K,V> p) {
            TreeNode<K,V> r, pp, rl;
            if (p != null && (r = p.right) != null) {
                if ((rl = p.right = r.left) != null)
                    rl.parent = p;
                if ((pp = r.parent = p.parent) == null)
                    (root = r).red = false;
                else if (pp.left == p)
                    pp.left = r;
                else
                    pp.right = r;
                r.left = p;
                p.parent = r;
            }
            return root;
        }

        static <K,V> TreeNode<K,V> rotateRight(TreeNode<K,V> root, TreeNode<K,V> p) {
            TreeNode<K,V> l, pp, lr;
            if (p != null && (l = p.left) != null) {
                if ((lr = p.left = l.right) != null)
                    lr.parent = p;
                if ((pp = l.parent = p.parent) == null)
                    (root = l).red = false;
                else if (pp.right == p)
                    pp.right = l;
                else
                    pp.left = l;
                l.right = p;
                p.parent = l;
            }
            return root;
        }

        final TreeNode<K, V> putTreeVal(HashMap源码分析<K, V> map, Node<K, V>[] tab,
                                        int h, K k, V v) {
            Class<?> kc = null;
            boolean searched = false;
            TreeNode<K, V> root = (parent != null) ? root() : this;
            for (TreeNode<K, V> p = root; ; ) {
                int dir, ph;
                K pk;
                if ((ph = p.hash) > h)
                    dir = -1;
                else if (ph < h)
                    dir = 1;
                else if ((pk = p.key) == k || (k != null && k.equals(pk)))
                    return p;
                else if ((kc == null &&
                        (kc = comparableClassFor(k)) == null) ||
                        (dir = compareComparables(kc, k, pk)) == 0) {
                    if (!searched) {
                        TreeNode<K, V> q, ch;
                        searched = true;
                        if (((ch = p.left) != null &&
                                (q = ch.find(h, k, kc)) != null) ||
                                ((ch = p.right) != null &&
                                        (q = ch.find(h, k, kc)) != null))
                            return q;
                    }
                    dir = tieBreakOrder(k, pk);
                }

                TreeNode<K, V> xp = p;
                if ((p = (dir <= 0) ? p.left : p.right) == null) {
                    Node<K, V> xpn = xp.next;
                    TreeNode<K, V> x = map.newTreeNode(h, k, v, xpn);
                    if (dir <= 0)
                        xp.left = x;
                    else
                        xp.right = x;
                    xp.next = x;
                    x.parent = x.prev = xp;
                    if (xpn != null)
                        ((TreeNode<K, V>) xpn).prev = x;
                    moveRootToFront(tab, balanceInsertion(root, x));
                    return null;
                }
            }
        }

    }

    //计算key的hash值，用于散列分布到数组索引上
    //hash函数大概的作用就是：高16bit不变，低16bit和高16bit做了一个异或，目的是减少碰撞
    //目的：int index = (n - 1) & hash(key);较少碰撞，散列均匀
    //如果直接使用key的hashcode()作为hash很容易发生碰撞。比如，在n - 1为15(0x1111)时，散列值真正生效的只是低4位。
    //当新增的键的hashcode()是2，18，34这样恰好以16的倍数为差的等差数列，就产生了大量碰撞。
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    //hashmap的核心方法：新增键值对
    public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }

    //hashmap的核心方法：通过key获取value
    public V get(Object key) {
        Node<K, V> e;
        return (e = getNode(hash(key), key)) == null ? null : e.value;
    }

    final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict) {
        Node<K, V>[] tab;
        Node<K, V> p;
        int n, i;
        //1.看table数组是否已经初始化
        if ((tab = table) == null || (n = tab.length) == 0) //还没有初始化table
            n = (tab = resize()).length;    //初始化table，分配内存

        //2.找到该key对应数组中的位置，(n - 1) & hash就是下标，前提：n是2的幂次方
        //在算index时，用位运算（n-1） & hash而不是模运算 hash % n的好处
        //2.1位运算消耗资源更少，更有效率
        //2.2避免了hashcode为负数的情况
        //为什么capcity是2的幂？
        //因为 算index时用的是（n-1） & hash，这样就能保证n -1是全为1的二进制数，如果不全为1的话，存在某一位为0，那么0，1与0与的结果都是0，这样便有可能将两个hash不同的值最终装入同一个桶中，造成冲突。所以必须是2的幂。
        if ((p = tab[i = (n - 1) & hash]) == null)
            //当前桶无元素，直接放就行了
            tab[i] = newNode(hash, key, value, null);
        else {
            //桶内有元素
            Node<K, V> e;
            K k;
            //key是否存在
            if (p.hash == hash &&
                    ((k = p.key) == key || (key != null && key.equals(k))))
                //桶内第一个元素的key等于待放入的key，用
                e = p;
            else if (p instanceof TreeNode) //如果节点类型是树，说明此时已经把链表转出红黑树了
                e = ((TreeNode<K, V>) p).putTreeVal(this, tab, hash, key, value);
            else {//桶内还是一个链表，则插入链尾（尾插）
                //此时还是链表结构的，没有转成红黑树，此时要做的事情有：
                //准备在链表上查找key是否已经存在，如果存在直接用新value值覆盖老value值
                //如果不存在，就需要在链表上插入节点，如果节点数超过8个，就将链表转成红黑树
                for (int binCount = 0; ; ++binCount) {
                    if ((e = p.next) == null) { //访问到链表尾部了，此时key没有重复，可以在链表尾部插入新节点了
                        p.next = newNode(hash, key, value, null);
                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                            treeifyBin(tab, hash);  //开始把链表转红黑树了
                        break;
                    }
                    if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k))))
                        //发现链表中key重复了，直接break，重复的节点保存在e中
                        break;
                    p = e;
                }
            }
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                afterNodeAccess(e);
                return oldValue;
            }
        }
        ++modCount;
        if (++size > threshold)  //检查是否应该扩容
            resize();
        afterNodeInsertion(evict);
        return null;
    }

    final Node<K, V> getNode(int hash, Object key) {
        Node<K, V>[] tab;
        Node<K, V> first, e;
        int n;
        K k;
        if ((tab = table) != null && (n = tab.length) > 0 &&
                (first = tab[(n - 1) & hash]) != null) { //底层数组已经初始化并且该key对应位置处节点不为空
            if (first.hash == hash && // always check first node
                    ((k = first.key) == key || (key != null && key.equals(k)))) //该桶上第一个节点就是我们要找的
                return first;
            if ((e = first.next) != null) {
                if (first instanceof TreeNode)  //桶已经树化
                    return ((TreeNode<K, V>) first).getTreeNode(hash, key);
                do {    //任然是链表，往下遍历就行了
                    if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k))))
                        return e;
                } while ((e = e.next) != null);
            }
        }
        return null;
    }

    //将hash对应table下标处的单向链表转成双向链表，然后再将这一个双向链表转成红黑树
    final void treeifyBin(Node<K, V>[] tab, int hash) {
        int n, index;
        Node<K, V> e;
        if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY) //如果table数组还没有被初始化，或者数组长度小于64
            resize();   //
        else if ((e = tab[index = (n - 1) & hash]) != null) {   //该hash对应table位置上不为空
            TreeNode<K, V> hd = null, tl = null;
            //do while循环目的：将单向链表转成双向链表
            do {
                TreeNode<K, V> p = replacementTreeNode(e, null); //将链表节点转成树节点
                if (tl == null)
                    hd = p;
                else {
                    p.prev = tl;
                    tl.next = p;
                }
                tl = p;
            } while ((e = e.next) != null);
            if ((tab[index] = hd) != null)  //将双向链表头结点地址存入该hash值对应的下标处
                hd.treeify(tab);    //着手开始将这一个双向链表转成红黑树了
        }
    }

    /**
     * resize扩容操作，主要用在两处：
     * 向一个空的HashMap中执行put操作时，会调用resize()进行初始化，要么默认初始化，capacity为16，要么根据传入的值进行初始化
     * put操作后，检查到size已经超过threshold，那么便会执行resize，进行扩容，如果此时capcity已经大于了最大值，那么便把threshold置为int最大值，否则，对capcity,threshold进行扩容操作。
     * 发生了扩容操作，那么必须Map中的所有的数进行再散列，重新装入。
     * <p>
     * 扩容后需要重新散列的原因:
     * 扩容前：某hash(key)=1000,n = 8,此时 (n-1) & hash = 0111 & 1000 = 0
     * 扩容后：                n = 16,此时(n-1) & hash = 1111 & 1000 = 1
     * 散列位置发生了变化，如果扩容后不重新散列，相同的hash(key)可能会散列到不同的索引上
     * <p>
     * 如果cap由8扩容16，
     * 8-1= 00111
     * 16-1=01111
     */
    final Node<K, V>[] resize() {
        Node<K, V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;  //当前的桶数
        int oldThr = threshold; //当前的扩容阈值
        int newCap, newThr = 0;
        if (oldCap > 0) {
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            } else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY && //2倍扩容
                    oldCap >= DEFAULT_INITIAL_CAPACITY)
                newThr = oldThr << 1; // double threshold
        } else if (oldThr > 0) {// initial capacity was placed in threshold
            newCap = oldThr;
        } else {               // zero initial threshold signifies using defaults
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        if (newThr == 0) {
            float ft = (float) newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float) MAXIMUM_CAPACITY ?
                    (int) ft : Integer.MAX_VALUE);
        }
        threshold = newThr;
        Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCap];  //准备扩容数组了
        table = newTab;
        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                Node<K, V> e;
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;   //gc
                    if (e.next == null) //当前桶上就一个节点
                        newTab[e.hash & (newCap - 1)] = e;
                    else if (e instanceof TreeNode) //当前桶已经树化
                        ((TreeNode<K, V>) e).split(this, newTab, j, oldCap);
                    else { //当前桶还是链表
                        Node<K, V> loHead = null, loTail = null;
                        Node<K, V> hiHead = null, hiTail = null;
                        Node<K, V> next;
                        do {
                            next = e.next;
                            //扩容前计算索引位置index = (n - 1) & hash，此时n = oldCap
                            // int oldIndex = (oldCap - 1) & hash;
                            //扩容后
                            //int newIndex = (2 * oldCap - 1) & hash;
                            //eg1：如果扩容前n = 4,扩容后就是n = 8
                            // 0011 & hash      0111 & hash  扩容前后索引位置是否发生变化取决于hash的第低三位是否是0还是1
                            //eg2：如果扩容前n = 8,扩容后就是n = 16
                            // 0111 & hash     1111 & hash 取决于第低四位是否是0还是1
                            //如果是0,0按位与任何数都是0，此时扩容后位置没有发生变化
                            //如果是1,此时扩容后位置会发生变化：此时位置如何变化呢？
                            //索引位置在高位多了一个1，就是增加了oldCap
                            //oldCap可能是 0010,0100,1000,...
                            if ((e.hash & oldCap) == 0) {   //该位置处扩容后位置不会变化
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            } else {    //该位置处扩容后位置发生改变
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead; //2倍扩容后索引位置不变
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;//2倍扩容后索引位置增加oldCap
                        }
                    }
                }
            }
        }
        return newTab;
    }

    /*计算大于等于cap的最小2次幂的数
    //原理：只需要关注二进制中第一个高位为1的位即可，其余为可以忽略
    *1.假设在第n位（......n n-1n-2 ......2 1）
    *2.无符号右移1位再按位或运算，则n n-1都是1           -->有2位为1
    *3.无符号右移2位再按位或运算，则n ......n-3都是1   -->有4位为1
    *4.无符号右移4位再按位或运算，则n ......n-7都是1   -->有8位为1
    *5.无符号右移8位再按位或运算，则n ......n-15都是1   -->有16位为1
    *6.无符号右移16位再按位或运算，则n ......n-31都是1   -->有32位为1
    *7.java int类型4字节，32位，正数符号位为0，且无符号右移时最多31位为1，即最大生成Interger.MAX_VALUE=0x7FFFFFFF
    */
    static final int tableSizeFor(int cap) {
        int n = cap - 1;//如果cap已经是2的幂，又没有执行这个减1操作，则执行完后面的几条无符号右移操作之后，返回的capacity将是这个cap的2倍
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    Node<K, V> replacementNode(Node<K, V> p, Node<K, V> next) {
        return new Node<>(p.hash, p.key, p.value, next);
    }

    Node<K, V> newNode(int hash, K key, V value, Node<K, V> next) {
        return new Node<>(hash, key, value, next);
    }

}