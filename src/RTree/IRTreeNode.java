package RTree;

import java.util.ArrayList;

interface IRTreeNode {
    void add(MBR rekt, IRTreeNode parent);
    void addLocally(IRTreeNode node);
    IRTreeNode[] split();

    ArrayList<MBR> search(MBR rekt);

    IRTreeNode readFromDisk(long id);
    void writeToDisk();
    void deleteFromDisk();

    long spaceUsed();
    int nodes();

    MBR getRectangle();
    int indexOf(MBR rekt);
    IRTreeNode getChild(int index);
    void deleteChild(long id);

    long getId();
    int size();
    boolean isFull();
    boolean isLeaf();

    ArrayList<Long> getChildren();
    ArrayList<MBR> getData();
    int getMin();
    int getReads();
    int getWrites();
}
