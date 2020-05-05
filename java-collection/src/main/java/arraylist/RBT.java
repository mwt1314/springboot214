package arraylist;

/**
 * @author mawt
 * @description
 * @date 2020/5/5
 */
public class RBT {

    private static class TreeNode {

        public TreeNode parent;

        public boolean red;

        public TreeNode left;

        public TreeNode right;

        public int key;

        public int value;

    }

    public static void main(String[] args) {

    }

    private TreeNode balanceInsertion(TreeNode root, TreeNode x) {
        x.red = true; //新插入节点为红色
        TreeNode xp, xpp, xppl, xppr;
        while (true) {
            if ((xp = x.parent) == null) {   //父节点不存在，说明x就是根节点
                x.red = false;  //根节点为黑色
                return x;
            }
            if (!xp.red) {   //插入节点的父节点为黑色
                //此时新插入节点为红色，并不会影响红黑树的自平衡
                return root;
            }
            //因为此时父节点xp为红色，那么一定存在根节点，xpp一定不会为空，同时xpp一定是黑色（红黑树的特性：红节点的直接后代一定是黑节点）
            xpp = xp.parent;    //祖父节点
            xppl = xpp.left;
            xppr = xpp.right;

            if (xppl != null && xppr != null) {//如果存在叔叔节点
                if (xppl.red && xppr.red) { //红色父节点，红色叔叔节点
                    xppl.red = false;
                    xppr.red = false;
                    xpp.red = true;
                    x = xpp;
                } else {    //红色父节点，黑色叔叔节点
                    if (xp == xppl) {    //父节点是祖父节点的左子树


                    } else {    //父节点是祖父节点的右子树

                    }

                }
            } else {    //不存在叔叔节点
                if (xp == xppl) {    //父节点是祖父节点的左子树
                    if (x == xp.left) { //插入节点是父节点的左子树
                        xp.red = false;
                        xpp.red = true;
                        root = rotateRight(root, xpp);
                    } else {    //右子树
                        root = rotateLeft(root, xp);
                        x = xp;
                    }
                } else {    //父节点是祖父节点的右子树
                    if (x == xp.left) { //插入节点是父节点的左子树
                        root = rotateRight(root, xp);
                        x = xp;
                    } else {
                        xp.red = false;
                        xpp.red = true;
                        root = rotateRight(root, xpp);
                    }
                }
            }
        }
    }

    //左旋
    private TreeNode rotateLeft(TreeNode root, TreeNode x) {
        //将右子树变成左子树
        TreeNode right = x.right, parent = x.parent;
        if (right == null) return root;
        boolean isLeft = parent != null && parent.left == x;
        TreeNode rl = right.left;
        x.parent = right;
        right.left = x;
        x.right = rl;
        if (parent == null) return right;
        if (isLeft) {
            parent.left = right;
        } else {
            parent.right = right;
        }
        return root;
    }

    //右旋
    private TreeNode rotateRight(TreeNode root, TreeNode x) {
        //将左子树变成右子树
        TreeNode left = x.left, parent = x.parent;
        if (left == null) return root;
        boolean isLeft = parent != null && parent.left == x;
        TreeNode lr = left.right;
        x.parent = left;
        left.left = x;
        x.left = lr;
        if (parent == null) return left;
        if (isLeft) {
            parent.left = left;
        } else {
            parent.right = left;
        }
        return root;
    }

}
