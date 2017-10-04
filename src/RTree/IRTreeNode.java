package RTree;

import java.util.ArrayList;

public interface IRTreeNode {
    void add(Long id);
    void addLocally(IRTreeNode node);
    IRTreeNode[] split();

    ArrayList<MBR> search(MBR rekt);

    IRTreeNode readFromDisk(Long id);
    void writeToDisk();
    void deleteFromDisk();

    MBR getRectangle();
    int indexOf(Long id);
    IRTreeNode getChild(int index);
    void deleteChild(Long id);

    Long getId();
    int size();
    boolean isFull();
    boolean isLeaf();
    boolean isMinimal();

    public ArrayList<Long> getChildren();
    public IRTreeNode getParent();
    public void setParent(Long parent);

    int getMax();
    int getMin();
}
