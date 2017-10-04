package RTree;

import java.util.ArrayList;

public interface IRTreeNode {
    void add(long id);
    void addLocally(IRTreeNode node);
    IRTreeNode[] split();

    ArrayList<MBR> search(MBR rekt);

    IRTreeNode readFromDisk(long id);
    void writeToDisk();
    void deleteFromDisk();

    MBR getRectangle();
    int indexOf(long id);
    IRTreeNode getChild(int index);
    void deleteChild(long id);

    long getId();
    int size();
    boolean isFull();
    boolean isLeaf();
    boolean isMinimal();

    public ArrayList<Long> getChildren();
    public IRTreeNode getParent();
    public void setParent(long parent);

    int getMax();
    int getMin();
}
