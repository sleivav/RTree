package RTree;

import java.io.Serializable;
import java.util.ArrayList;

abstract class AbstractRTreeNode implements IRTreeNode, Serializable {
    private MBR rectangle;
    private ArrayList<Integer> data;
    private ArrayList<Long> children;
    private Long id;
    private int m;
    private int M;

    public AbstractRTreeNode(boolean isLeaf) {
        this.id = IdGenerator.nextId();
        this.rectangle = null;
        this.data = new ArrayList<Integer>(M);
    }

    public AbstractRTreeNode(IRTreeNode child) {
        this(false);
        children.add(child.getId());
        //split?
    }

    @Override
    public void add(Long id) {
        IRTreeNode node = this;
        while (!node.isLeaf()) {
            double d = node.indexOf(id);
            int i = (int) d; // ??
            if (i == d) {
                return;
            } else {
                IRTreeNode child = node.getChild((long) i);
                if (child.isFull()) {
                    // split
                } else {
                    node.writeToDisk();
                    node = child;
                }
            }
        }
        node.addLocally(id);
        node.writeToDisk();
    }

    @Override
    public void addLocally(Long id) {

    }

    @Override
    public IRTreeNode createSibling() {
        return null;
    }

    @Override
    public void deleteFromDisk() {

    }

    @Override
    public void writeToDisk() {

    }

    @Override
    public MBR getRectangle() {
        return rectangle;
    }

    @Override
    public IRTreeNode getChild(Long index) {
        return null;
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public double indexOf(Long id) {
        return 0;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public boolean isMinimal() {
        return false;
    }


    @Override
    public IRTreeNode readFromDisk(Long id) {
        return null;
    }


    @Override
    public Long size() {
        return null;
    }
}
