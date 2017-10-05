package RTree;

class LinearRTree extends RTree {
    @Override
    public IRTreeNode newNode(boolean isLeaf) {
        return new LinearNode(isLeaf);
    }
}
