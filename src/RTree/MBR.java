package RTree;

import java.io.Serializable;
import java.util.ArrayList;

public class MBR implements Serializable {
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

    public MBR copy() {
        return new MBR(left, bottom, right, top);
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

    public double area() {
        return (right - left) * (top - bottom);
    }

    public boolean intersecc(MBR rect) {
        boolean interX = (left <= rect.left && rect.left <= right) || (left <= rect.right && rect.right <= right);
        boolean interY = (bottom <= rect.bottom && rect.bottom <= top) || (bottom <= rect.top && rect.top <= top);
        return interX && interY;
    }

    public double getLeft() {
        return left;
    }

    public double getRight() {
        return right;
    }

    public double getBottom() {
        return bottom;
    }

    public double getTop() {
        return top;
    }

    @Override
    public String toString() {
        return "p1: (" + left + ", " + bottom + ") p2: (" + right + ", " + top + ")";
    }
}
