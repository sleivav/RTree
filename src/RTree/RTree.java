package RTree;

import java.io.File;
import java.util.ArrayList;

public abstract class RTree implements IRTree {
    static String DIR = RTree.class.getProtectionDomain().getCodeSource().getLocation().getFile()
            + File.separator;

    IRTreeNode root;

    public RTree() {
        root = newNode();
        root.writeToDisk();
    }

    @Override
    public void add(MBR rekt) {
        root.add(rekt, null);
        root = root.readFromDisk(root.getId());

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

    float blockUsage() {
        return root.spaceUsed() / root.nodes() / 512.0f;
    }

    abstract public IRTreeNode newNode();
}
