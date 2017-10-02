package RTree;

public interface IRTreeNode {
    void add(Long id);
    void addLocally(Long id);
    IRTreeNode split();
    IRTreeNode createSibling();

    IRTreeNode readFromDisk(Long id);
    void writeToDisk();
    void deleteFromDisk();

    IRTreeNode getChild(Long index);
    double indexOf(Long id);

    Long getId();
    Long size();
    boolean isFull();
    boolean isLeaf();
    boolean isMinimal();
}
