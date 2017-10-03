package RTree;

public class GreenesRTree extends RTree {
    public static void main(String[] args) {
        GreenesRTree tree = new GreenesRTree();

        int rekts = 100;
        for (int i = 0; i < rekts; i++) {
            IRTreeNode node = new GreenesSplitRTreeNode(getRekt());
            node.writeToDisk();
            tree.add(node.getId());
        }
    }

    public IRTreeNode newNode() {
        return new GreenesSplitRTreeNode();
    }
}
