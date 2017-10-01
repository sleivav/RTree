package RTree;

import java.util.ArrayList;

public interface IMBR {
    MBRPair search(IMBR leafEntry);
    IMBR splitWhenFull();
    void updateNodes(IMBR newChild);
    void updateNodes(IMBR newChild, MBRPair pair);
    void addNonLeafChild(IMBR child);
    void addNonLeafChild(IMBR newChild, MBRPair pair);
    void addLeafChild(IMBR child);
    void addLeafChild(IMBR newChild, MBRPair pair);
    IMBR splitLeaf();
    IMBR splitNonLeaf();
    void adjustRegion(IMBR newRegionMBR);
    Double getArea();
    IMBR getParent();
    void setParent(IMBR parent);
    ArrayList<IMBR> getChildren();
    Integer getId();
    ArrayList<IMBR> getLeaves();
    IMBR getRoot();
}
