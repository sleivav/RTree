package RTree;

import java.util.ArrayList;

public class MBR {
    private double left, bottom, right, top;

    public MBR(double left, double bottom, double right, double top) {
        this.left = left;
        this.bottom = bottom;
        this.right = right;
        this.top = top;
    }

    public MBR(ArrayList<IRTreeNode> nodes) {
        left = Double.MAX_VALUE;
        right = Double.MIN_VALUE;
        bottom = Double.MAX_VALUE;
        top = Double.MIN_VALUE;
        nodes.forEach(this::update);
    }

    public void update(IRTreeNode node) {
        left = Math.min(left, node.getRectangle().left);
        right = Math.max(right, node.getRectangle().right);
        bottom = Math.min(bottom, node.getRectangle().bottom);
        top = Math.max(top, node.getRectangle().top);
    }

    public double calcChange(IRTreeNode node) {
        double newLeft = Math.min(left, node.getRectangle().left);
        double newRight = Math.max(right, node.getRectangle().right);
        double newBottom = Math.min(bottom, node.getRectangle().bottom);
        double newTop = Math.max(top, node.getRectangle().top);

        double newArea = (newRight - newLeft) * (newTop - newBottom);
        return newArea - area();
    }

    private double area() {
        return (right - left) * (top - bottom);
    }

    public double xDistance(MBR rect) {
        return Math.min(right, rect.right) - Math.max(left, rect.left);
    }

    public double yDistance(MBR rect) {
        return Math.min(top, rect.top) - Math.max(bottom, rect.bottom);
    }
}
