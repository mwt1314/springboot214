package cn.matio.interview_internal_reference.ali1;

/**
 * 01.阿里篇/1.1.3 给定一个二叉搜索树(BST)，找到树中第 K 小的节点.md
 * https://github.com/0voice/interview_internal_reference/blob/master/01.%E9%98%BF%E9%87%8C%E7%AF%87/1.1.3%20%E7%BB%99%E5%AE%9A%E4%B8%80%E4%B8%AA%E4%BA%8C%E5%8F%89%E6%90%9C%E7%B4%A2%E6%A0%91(BST)%EF%BC%8C%E6%89%BE%E5%88%B0%E6%A0%91%E4%B8%AD%E7%AC%AC%20K%20%E5%B0%8F%E7%9A%84%E8%8A%82%E7%82%B9.md
 * <p>
 * 知识点：
 * 1.树的概念
 * I.二叉树：每个节点最多含有两个子树的树
 * II.满二叉树：除最后一层无任何子节点外，每一层上的所有结点都有两个子结点
 * III.完全二叉树
 * IV.二叉搜索树（Binary Search Tree）：指一棵空树或者具有下列性质的二叉树
 * 1.若任意节点的左子树不空，则左子树上所有节点的值均小于它的根节点的值；
 * 2. 若任意节点的右子树不空，则右子树上所有节点的值均大于它的根节点的值；
 * 3.任意节点的左、右子树也分别为二叉查找树；
 * 4.没有键值相等的节点
 * V.平衡二叉树：当且仅当任何节点的两棵子树的高度差不大于1的二叉树
 * V.红黑树
 * VI.B+树
 * VII.B-树
 * VIII.哈夫曼树（Huffman Tree）：一种带权路径长度最短的二叉树，也称为最优二叉树
 * <p>
 * <p>
 * <p>
 * 2.树的遍历方式
 * I.前序遍历：根 左 右
 * II.中序遍历：左 根 右
 * III.后序遍历：左 右 根
 * <p>
 * 分析思路：
 * 1.如果某个根节点的左子树的数目=K-1，则该节点就是目标节点
 * 2.如果小于K-1，则目标节点一定在右子树，否则在左子树
 *
 * 分析思路2：二叉搜索树按照中序遍历的顺序打印出来正好就是排序好的顺序，所以，按照中序遍历顺序找到第k个结点就是结果
 *
 */
public class Ali3 {

    //二叉树定义
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    private class ResultType {

        boolean found;  // 是否找到

        int val;  // 节点数目

        ResultType(boolean found, int val) {
            this.found = found;
            this.val = val;
        }
    }

    public int kthSmallest(TreeNode root, int k) {
        return kthSmallestHelper(root, k).val;
    }

    private ResultType kthSmallestHelper(TreeNode root, int k) {
        if (root == null) {
            return new ResultType(false, 0);
        }

        ResultType left = kthSmallestHelper(root.left, k);

        // 左子树找到，直接返回
        if (left.found) {
            return new ResultType(true, left.val);
        }

        // 左子树的节点数目 = K-1，结果为 root 的值
        if (k - left.val == 1) {
            return new ResultType(true, root.val);
        }

        // 右子树寻找
        ResultType right = kthSmallestHelper(root.right, k - left.val - 1);
        if (right.found) {
            return new ResultType(true, right.val);
        }

        // 没找到，返回节点总数
        return new ResultType(false, left.val + 1 + right.val);
    }

    private static int index = 0;

    //方法2：中序遍历
    private static TreeNode method2(TreeNode root, int k) {
        if (root != null) {
            TreeNode node = method2(root.left, k);
            if (node != null) {
                return node;
            }
            index++;
            if (index == k) {
                return root;
            }
            node = method2(root.right, k);
            if (node != null) {
                return node;
            }
        }
        return null;
    }


    public static void main(String[] args) {
        TreeNode root = new TreeNode(5);
        int k = 3;
    }

    //前序遍历
    private static void before(TreeNode root) {
        if (root != null) {
            System.out.print(root.val + "\t");
            before(root.left);
            before(root.right);
        }
    }

    //中序遍历
    private static void mid(TreeNode root) {
        if (root != null) {
            mid(root.left);
            System.out.print(root.val + "\t");
            mid(root.right);
        }
    }

    //后序遍历
    private static void after(TreeNode root) {
        if (root != null) {
            after(root.left);
            after(root.right);
            System.out.print(root.val + "\t");
        }
    }

}
