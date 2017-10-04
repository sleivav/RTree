package RTree;

import java.util.ArrayList;

public class LinearRTree extends RTree {

    public LinearRTree(IRTreeNode node1, IRTreeNode node2, long seed) {
        super(seed);
        root.addLocally(node1);
        root.addLocally(node2);
    }

    @Override
    public IRTreeNode newNode() {
        return new LinearSplitRTreeNode();
    }
}
