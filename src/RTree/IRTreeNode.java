package RTree;

public interface IRTreeNode {
    void add(Long id);
    void addLocally(Long id);
    IRTreeNode split();

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
}
