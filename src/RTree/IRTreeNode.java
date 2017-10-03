package RTree;

import java.util.ArrayList;

public interface IRTreeNode {
    void add(Long id);
    void addLocally(IRTreeNode node);
    IRTreeNode[] split();

    IRTreeNode readFromDisk(Long id);
    void writeToDisk();
    void deleteFromDisk();

    MBR getRectangle();
    IRTreeNode getChild(int index);
    int indexOf(Long id);

    Long getId();
    int size();
    boolean isFull();
    boolean isLeaf();
    boolean isMinimal();

    public ArrayList<Long> getChildren();
    public IRTreeNode getParent();
    public void setChildren(ArrayList<Long> children);
    public void setParent(Long parent);

    int getMax();
    int getMin();
}
