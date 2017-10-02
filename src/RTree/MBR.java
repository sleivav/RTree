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
        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;
        for (IRTreeNode node: nodes) {
            minX = Math.min(minX, node.getRectangle().left)
        }
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
