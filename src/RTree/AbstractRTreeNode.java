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

            System.out.println(children.size() + " children");
            System.out.println(data.size() + " data");
        } else {
            data.add(rekt);
        }

        if (size() == 1)
            rectangle = rekt.copy();
        else
            rectangle.update(rekt);


        if (isFull() && parent != null) {
            System.out.println("split");
            IRTreeNode[] newNodes = this.split();

            parent.getChildren().remove(this.getId());
            parent.getData().remove(this.getRectangle());

            parent.addLocally(newNodes[0]);
            parent.addLocally(newNodes[1]);
            parent.writeToDisk();
        } else {
            // TODO es necesario solo acá?
            writeToDisk();
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
    }

    @Override
    public ArrayList<MBR> search(MBR rekt) {
        ArrayList<MBR> res = new ArrayList<>();

        if (isLeaf()) {
            for (int i = 0; i < size(); i++) {
                if (data.get(i).intersecc(rekt))
                    res.add(data.get(i));
            }
            return res;
        }

        for (int i = 0; i < size(); i++) {
            if (data.get(i).intersecc(rekt))
                res.addAll(getChild(i).search(rekt));
        }
        return res;
    }

    @Override
    public void deleteFromDisk() {
        System.out.println("delete node " + getId());

        try {
            File file = new File(RTree.DIR + "r" + id + ".node");
            // file.delete();
            if (!file.delete()) throw new Exception("failed to delete file");
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
        int index = 0;
        float min_change = Float.MAX_VALUE;
        float min_area = Float.MAX_VALUE;

        for (int i = 0; i < size(); i++) {
            MBR rekt = data.get(i);
            float change = rekt.calcChange(rect);

            if (change < min_change) {
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
        return data.size();
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
