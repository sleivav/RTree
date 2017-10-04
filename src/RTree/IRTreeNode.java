package RTree;

import java.util.ArrayList;

interface IRTreeNode {
    void add(long id);
    void addLocally(IRTreeNode node);
    IRTreeNode[] split();

    ArrayList<MBR> search(MBR rekt);

    IRTreeNode readFromDisk(long id);
    void writeToDisk();
    void deleteFromDisk();

    long spaceUsed();
    int nodes();

    MBR getRectangle();
    int indexOf(long id);
    IRTreeNode getChild(int index);
    void deleteChild(long id);

    long getId();
    int size();
    boolean isFull();
    boolean isLeaf();

    ArrayList<Long> getChildren();
    IRTreeNode getParent();
    void setParent(long parent);
    int getMin();

}
