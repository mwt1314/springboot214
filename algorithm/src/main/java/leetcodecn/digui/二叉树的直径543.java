package leetcodecn.digui;

public class 二叉树的直径543 {

    public static void main(String[] args) {
        System.out.println("题目来源：https://leetcode-cn.com/problems/diameter-of-binary-tree/");
        System.out.println("543. 二叉树的直径：给定一棵二叉树，你需要计算它的直径长度。一棵二叉树的直径长度是任意两个结点路径长度中的最大值。这条路径可能穿过根结点。" +
                "示例 :" +
                "给定二叉树" +
                "          1" +
                "         / \\" +
                "        2   3" +
                "       / \\     " +
                "      4   5    " +
                "返回 3, 它的长度是路径 [4,2,1,3] 或者 [5,2,1,3]。" +
                "注意：两结点之间的路径长度是以它们之间边的数目表示");
    }

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    public int diameterOfBinaryTree(TreeNode root) {
        int leftHeight = 0, rightHeight = 0;
        if (root == null)
            return 0;
        int left = getHeight(root.left);
        int right = getHeight(root.right);

        return right + left;
    }


    private int getHeight(TreeNode root) {
        if (root == null)
            return 0;
        int left = getHeight(root.left);
        int right = getHeight(root.right);
        return 1 + Math.max(left, right);
    }

}
