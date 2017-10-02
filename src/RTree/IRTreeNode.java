package RTree;

public interface IRTreeNode {
    void add(Long id);
    void addLocally(Long id);
    IRTreeNode createSibling();
    void deleteFromDisk();
    void writeToDisk();
    IRTreeNode getChild(Long index);
    Long getId();
    double indexOf(Long id);
    boolean isFull();
    boolean isLeaf();
    boolean isMinimal();
    IRTreeNode split();
    IRTreeNode readFromDisk(Long id);
    Long size();
}
