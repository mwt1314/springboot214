package arraylist;

/**
 * @author mawt
 * @description
 * @date 2020/6/2
 */
public class AVLTest {

    private static TreeNode root;

    private static class TreeNode {
        public int value;
        public TreeNode left;
        public TreeNode right;
        public TreeNode parent;

        public TreeNode(int value, TreeNode left, TreeNode right, TreeNode parent) {
            this.value = value;
            this.left = left;
            this.right = right;
            this.parent = parent;
        }

    }

    public static void main(String[] args) {

    }

    private static void buildTree(int v) {
        if (root != null) {
            TreeNode parent = findParent(v);
            if (parent != null) {
                TreeNode node = new TreeNode(v, null, null, parent);
                if (parent.value > v) parent.left = node;
                else  parent.right = node;
            }
        } else {
            root = new TreeNode(v, null, null, null);
        }
    }

    private static TreeNode findParent(int v) {
        TreeNode node = root;
        while (node != null) {
            if (node.value == v) return null;
            else if (node.value > v) {
                if (node.left == null) return node;
                node = node.left;
            }
            else {
                if (node.right == null) return node;
                node = node.right;
            }
        }
        return null;
    }





}
