package RTree;

import java.io.*;
import java.util.ArrayList;

abstract class AbstractRTreeNode implements IRTreeNode, Serializable {
    private MBR rectangle;
    private Long parent;
    private ArrayList<Long> children;
    private Long id;
    private final int min = 0;
    private final int max = 0;

    public AbstractRTreeNode() {
        this.id = IdGenerator.nextId();
        this.parent = null;
        this.rectangle = null;
        this.children = new ArrayList<Long>(max);
    }

    public AbstractRTreeNode(IRTreeNode child) {
        this();
        children.add(child.getId());
    }

    @Override
    public void add(Long id) {
        IRTreeNode node = this;
        while (!node.isLeaf()) {
            int i = node.indexOf(id);
            node = node.getChild(i);
        }
        node = node.getParent();

        node.addLocally(readFromDisk(id));
        node.writeToDisk();
    }

    @Override
    public void addLocally(IRTreeNode node) {
        children.add(node.getId());
        node.setParent(this.getId());

        if (isFull() && parent != null) {
            IRTreeNode[] newNodes = split();
            IRTreeNode parent = getParent();

            parent.deleteChild(getId());

            parent.addLocally(newNodes[0]);
            parent.addLocally(newNodes[1]);
        } else {
            rectangle.update(node);
            writeToDisk();
        }
    }

    @Override
    public ArrayList<MBR> search(MBR rekt) {
        // TODO implement
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
        }
        return readFromDisk(children.get(index));
    }

    @Override
    public void deleteChild(Long id) {
        children.remove(id);
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
        return size() == max;
    }

    @Override
    public boolean isLeaf() {
        return children == null;
    }

    @Override
    public boolean isMinimal() {
        return size() > min;
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

    @Override
    public IRTreeNode getParent() {
        return readFromDisk(parent);
    }

    @Override
    public void setParent(Long parent) {
        this.parent = parent;
    }

    @Override
    public int getMin() {
        return min;
    }

    @Override
    public int getMax() {
        return max;
    }
}
