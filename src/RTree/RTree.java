package RTree;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public abstract class RTree implements IRTree {
    public static String DIR = RTree.class.getProtectionDomain().getCodeSource().getLocation().getFile()
            + File.separator;
    IRTreeNode root;
    private static Random rand = new Random();

    public RTree() {
        root = newNode();
        root.writeToDisk();
    }

    @Override
    public void add(long id) {
        root.add(id);
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

    abstract public IRTreeNode newNode();

    public static MBR getRekt() {
        Double x = rand.nextDouble() * 500000;
        Double y = rand.nextDouble() * 500000;

        Double width = rand.nextDouble() * 100;
        Double height = rand.nextDouble() * 100;

        return new MBR(x, y, x + width, y + height);
    }
}
