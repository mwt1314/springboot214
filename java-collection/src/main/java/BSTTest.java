import java.security.Key;

/**
 * @author mawt
 * @description
 * @date 2020/4/16
 */
public class BSTTest {

    private class Node<Key, Value> {

        private Key key;
        private Value val;
        private Node left, right;
        private int size;

        public Node(Key key, Value val, int n) {
            this.key = key;
            this.val = val;
            this.size = n;
            left = right = null;
        }
    }

    public <Key, Value> Value get(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        return get(null, key);
    }

    private <Key, Value> Value get(Node x, Key key) {
        if (x == null) return null;

    }

    private String BSTString(Node x) {

        if (x == null) {
            return "";
        }

        String s = "";

        s += BSTString(x.left);
        s += x.key + " " + x.val + " " + x.size + "(s)\n";
        s += BSTString(x.right);

        return s;
    }


}
