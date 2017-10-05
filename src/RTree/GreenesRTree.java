package RTree;

class GreenesRTree extends RTree {
    @Override
    public IRTreeNode newNode(boolean isLeaf) {
        return new GreenesNode(isLeaf);
    }
}
