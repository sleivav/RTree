package RTree;

import java.io.*;
import java.util.ArrayList;

abstract class AbstractRTreeNode implements IRTreeNode, Serializable {
    private MBR rectangle;
    private long parent;
    private ArrayList<Long> children;
    private long id;
    private final int min = 0;
    private final int max = 8;

    public AbstractRTreeNode() {
        this.id = IdGenerator.nextId();
        this.parent = -1;
        this.children = new ArrayList<Long>(max);
        this.rectangle = null;
    }

    public AbstractRTreeNode(MBR rectangle) {
        this.id = IdGenerator.nextId();
        this.parent = -1;
        this.children = null;
        this.rectangle = rectangle;
    }

    @Override
    public void add(long id) {
        IRTreeNode node = this;
        IRTreeNode aux = node;
        while (!aux.isLeaf()) {
            node = aux;
            int i = node.indexOf(id);
            aux = node.getChild(i);
        }

        node.addLocally(readFromDisk(id));
    }

    @Override
    public void addLocally(IRTreeNode node) {
        children.add(node.getId());
        node.setParent(getId());

        if (isFull() && parent != -1) {
            IRTreeNode[] newNodes = split();
            IRTreeNode parent = getParent();

            parent.deleteChild(getId());

            parent.addLocally(newNodes[0]);
            parent.addLocally(newNodes[1]);
        } else {
            if (size() == 1) {
                rectangle = node.getRectangle().copy();
            }
            rectangle.update(node);
            this.writeToDisk();
        }
    }

    @Override
    public ArrayList<MBR> search(MBR rekt) {
        ArrayList<MBR> res = new ArrayList<MBR>();
        if (isLeaf()) {
            if (getRectangle().intersecc(rekt)) {
                res.add(getRectangle());
            }
            return res;
        }
        for (int i = 0; i < size(); i++) {
            res.addAll(getChild(i).search(rekt));
        }
        return res;
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
    public IRTreeNode readFromDisk(long id) {
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
    public void deleteChild(long id) {
        children.remove(id);
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public int indexOf(long id) {
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
    public IRTreeNode getParent() {
        return readFromDisk(parent);
    }

    @Override
    public void setParent(long parent) {
        this.parent = parent;
        writeToDisk();
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
