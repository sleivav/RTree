package RTree;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

abstract class AbstractRTreeNode implements IRTreeNode, Serializable {
    private MBR rectangle;
    private ArrayList<Long> children;
    private Long id;
    private int m;
    private int M;

    public AbstractRTreeNode(boolean isLeaf) {
        this.id = IdGenerator.nextId();
        this.rectangle = null;
        this.children = new ArrayList<Long>(M);
    }

    public AbstractRTreeNode(IRTreeNode child) {
        this(false);
        children.add(child.getId());
        //split?
    }

    @Override
    public void add(Long id) {
        IRTreeNode node = this;
        while (!node.isLeaf()) {
            int i = node.indexOf(id);
            IRTreeNode child = node.getChild(i);
            if (child.isFull()) {
                // split
            } else {
                node.writeToDisk();
                node = child;
            }
        }
        node.addLocally(id);
        node.writeToDisk();
    }

    @Override
    public void addLocally(Long id) {
        double d = indexOf(id);
        int i = (int) d;
        if (i != d) {
            children.add(i, id);
        }
    }

    @Override
    public IRTreeNode createSibling() {
        return null;
    }

    @Override
    public void deleteFromDisk() {
        try {
            File file = new File(RTree.DIR + "r" + id + ".node");
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void writeToDisk() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(RTree.DIR + "r" + id + ".node"));
            out.writeObject(this);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public MBR getRectangle() {
        return rectangle;
    }

    @Override
    public IRTreeNode getChild(int index) {
        if (isLeaf()) {
            return null;
        } else {
            return readFromDisk(children.get(index));
        }
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public int indexOf(Long id) {
        IRTreeNode node = readFromDisk(id);
        int index = 0;
        double min_change = Double.MAX_VALUE;
        double min_area = Double.MAX_VALUE;
        for(int i = 0; i < size(); i++) {
            IRTreeNode act = this.getChild(i);
            MBR rekt = act.getRectangle();
            double change = rekt.calcChange(node);
            if (change > min_change) {
                index = i;
                min_change = change;
                min_area = rekt.area();
            } else if (change == min_change && rekt.area() < min_area) {
                index = i;
                min_area = rekt.area();
            }
        }
        return index;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public boolean isMinimal() {
        return false;
    }


    @Override
    public IRTreeNode readFromDisk(Long id) {
        return null;
    }


    @Override
    public Long size() {
        return null;
    }
}
