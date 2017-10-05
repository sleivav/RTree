package RTree;

import java.io.*;
import java.util.ArrayList;

abstract class RTreeNode implements IRTreeNode, Serializable {
    private long id;

    private MBR rectangle;
    private ArrayList<MBR> data;
    private ArrayList<Long> children;

    private final int max = 100;
    private static int reads = 0;
    private static int writes = 0;

    RTreeNode(boolean isLeaf) {
        id = IdGenerator.nextId();
        rectangle = null;
        data = new ArrayList<>(max);

        if (isLeaf)
            children = null;
        else
            children = new ArrayList<>(max);
    }

    RTreeNode(MBR rectangle) {
        id = IdGenerator.nextId();
        this.rectangle = rectangle.copy();
        children = null;

        data = new ArrayList<>(max);
        data.add(rectangle);
    }

    @Override
    public void add(MBR rekt, IRTreeNode parent) {

        if (!isLeaf()) {
            if (size() != getChildren().size())
                fix(); // bugfix

            int i = indexOf(rekt);
            data.get(i).update(rekt);
            getChild(i).add(rekt, this);
        } else {
            data.add(rekt);
        }

        if (size() == 1)
            rectangle = rekt.copy();
        else
            rectangle.update(rekt);


        if (isFull() && parent != null) {
            IRTreeNode[] newNodes = this.split();

            parent.getChildren().remove(this.getId());
            parent.getData().remove(this.getRectangle());

            parent.addLocally(newNodes[0]);
            parent.addLocally(newNodes[1]);
            parent.writeToDisk();
        } else {
            writeToDisk();
        }
    }


    private void fix() {
        data = new ArrayList<>(max);
        for (int i = 0; i < children.size(); i++) {
            data.add(getChild(i).getRectangle().copy());
        }
    }

    @Override
    public void addLocally(IRTreeNode node) {
        children.add(node.getId());
        data.add(node.getRectangle());

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
        try {
            File file = new File(RTree.DIR + "r" + id + ".node");
            if (!file.delete()) throw new Exception("failed to delete file");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public IRTreeNode readFromDisk(long id) {
        reads++;
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(RTree.DIR + "r" + id + ".node"));
            RTreeNode node = (RTreeNode) (in.readObject());
            in.close();
            return node;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    @Override
    public void writeToDisk() {
        writes++;
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

        if (!isLeaf())
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
    public int getMin() {
        return (int) (0.4 * max);
    }

    @Override
    public int getReads() {
        return reads;
    }

    @Override
    public int getWrites() {
        return writes;
    }

    static void reset() {
        reads = writes = 0;
    }
}
