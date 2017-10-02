package RTree;

import java.util.ArrayList;

public class MBR {
    double left, right, bottom, top;

    public MBR(double left, double bottom, double right, double top) {
        this.left = left;
        this.bottom = bottom;
        this.right = right;
        this.top = top;
    }

    public MBR(ArrayList<IRTreeNode> nodes) {

    }

    public void update(IRTreeNode node) {

    }

    public double calcChange(IRTreeNode node) {
        double change = 0;

        return change;
    }

    public double distance(MBR rectangle, int dimension) {
        double dist = 0;

        return dist;
    }
}
