package RTree;

import java.io.File;
import java.util.ArrayList;

public abstract class RTree implements IRTree {
    public static String DIR = RTree.class.getProtectionDomain().getCodeSource().getLocation().getFile()
            + File.separator;
    IRTreeNode root;

    public RTree() {
        root = newNode();
        root.writeToDisk();
    }

    @Override
    public void add(Long id) {
        root.add(id);

        if (root.isFull()) {
            IRTreeNode parent = newNode();

            IRTreeNode[] newNodes = root.split();
            parent.addLocally(newNodes[0]);
            parent.addLocally(newNodes[1]);

            root = parent;
        }
    }

    @Override
    public ArrayList<MBR> search(MBR rectangle) {
        return root.search(rectangle);
    }

    abstract public IRTreeNode newNode();
}
