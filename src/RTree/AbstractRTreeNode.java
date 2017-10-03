package RTree;

import java.io.*;
import java.util.ArrayList;

abstract class AbstractRTreeNode implements IRTreeNode, Serializable {
    private MBR rectangle;
    private ArrayList<Long> children;
    private Long id;
    private final int m = 0;
    private final int M = 0;

    public AbstractRTreeNode() {
        this.id = IdGenerator.nextId();
        this.rectangle = null;
        this.children = new ArrayList<Long>(M);
    }

    public AbstractRTreeNode(IRTreeNode child) {
        this();
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
            // TODO update MBR
        }
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
    public IRTreeNode readFromDisk(Long id) {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(RTree.DIR + "r" + id + ".node"));
            return (AbstractRTreeNode) (in.readObject());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
            return null;
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
        for (int i = 0; i < size(); i++) {
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
        return size() == M;
    }

    @Override
    public boolean isLeaf() {
        return children == null;
    }

    @Override
    public boolean isMinimal() {
        return size() > m;
    }

    @Override
    public int size() {
        return children.size();
    }

    @Override
    public ArrayList<Long> getChildren() {
        return children;
    }

    @Override
    public void setChildren(ArrayList<Long> children) {
        // TODO update MBR
        this.children = children;
    }
}
