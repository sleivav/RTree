package RTree;

import java.io.*;
import java.util.ArrayList;

abstract class AbstractRTreeNode implements IRTreeNode, Serializable {
    private long id;

    private MBR rectangle;
    private ArrayList<MBR> data;

    private long parent;
    private ArrayList<Long> children;

    private final int max = 8;

    AbstractRTreeNode() {
        id = IdGenerator.nextId();
        parent = -1;
        children = new ArrayList<>(max);
        rectangle = null;

        data = new ArrayList<>(max);
    }

    AbstractRTreeNode(MBR rectangle) {
        id = IdGenerator.nextId();
        parent = -1;
        children = null;
        this.rectangle = rectangle;

        data = new ArrayList<>(max);
        data.add(rectangle);
    }

    @Override
    public void add(MBR rekt, IRTreeNode parent) {
        if (!isLeaf()) {
            int i = indexOf(rekt);
            getChild(i).add(rekt, this);
            data.get(i).update(rekt);
        } else {
            data.add(rekt);
        }

        if (isFull() && parent != null) {
            IRTreeNode[] newNodes = split();

            parent.getChildren().remove(getId());
            parent.getData().remove(getRectangle());

            parent.addLocally(newNodes[0]);
            parent.addLocally(newNodes[1]);
        }
    }

    @Override
    public void addLocally(IRTreeNode node) {
        children.add(node.getId());
        data.add(node.getRectangle());

        // node.setParent(getId());

        if (size() == 1)
            rectangle = node.getRectangle().copy();
        else
            rectangle.update(node.getRectangle());

        writeToDisk();
    }

    @Override
    public ArrayList<MBR> search(MBR rekt) {
        ArrayList<MBR> res = new ArrayList<>();
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
            if (!file.delete())
                throw new Exception("failed to delet file");
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
    public long spaceUsed() {
        File f = new File(RTree.DIR + "r" + id + ".node");
        long used = f.length();

        if (children != null)
            for (Long id : children)
                used += readFromDisk(id).spaceUsed();

        return used;
    }

    @Override
    public int nodes() {
        int n = 1;
        if (children != null)
            for (Long id : children)
                n += readFromDisk(id).nodes();

        return n;
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
    public int indexOf(MBR rect) {
        IRTreeNode node = readFromDisk(id);
        int index = 0;
        float min_change = Float.MAX_VALUE;
        float min_area = Float.MAX_VALUE;

        for (int i = 0; i < size(); i++) {
            IRTreeNode act = this.getChild(i);
            MBR rekt = act.getRectangle();
            float change = rekt.calcChange(rect);

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
    public int size() {
        return children.size();
    }

    @Override
    public ArrayList<Long> getChildren() {
        return children;
    }

    @Override
    public ArrayList<MBR> getData() {
        return data;
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
        return (int) (0.4 * max);
    }
}
