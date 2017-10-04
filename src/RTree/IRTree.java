package RTree;

import java.io.Serializable;
import java.util.ArrayList;

public interface IRTree extends Serializable {
    void add(long id);
    ArrayList<MBR> search(MBR rectangle);
}
