package RTree;

import java.io.Serializable;
import java.util.ArrayList;

interface IRTree extends Serializable {
    void add(MBR rekt);
    ArrayList<MBR> search(MBR rectangle);
    IRTreeNode newNode(boolean idLeaf);
}
