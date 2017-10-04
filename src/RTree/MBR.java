package RTree;

import java.io.Serializable;
import java.util.ArrayList;

public class MBR implements Serializable {
    private float left, bottom, right, top;

    public MBR(float left, float bottom, float right, float top) {
        this.left = left;
        this.bottom = bottom;
        this.right = right;
        this.top = top;
    }

    public MBR(ArrayList<IRTreeNode> nodes) {
        left = Float.MAX_VALUE;
        right = Float.MIN_VALUE;
        bottom = Float.MAX_VALUE;
        top = Float.MIN_VALUE;
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

    public float calcChange(IRTreeNode node) {
        float newLeft = Math.min(left, node.getRectangle().left);
        float newRight = Math.max(right, node.getRectangle().right);
        float newBottom = Math.min(bottom, node.getRectangle().bottom);
        float newTop = Math.max(top, node.getRectangle().top);

        float newArea = (newRight - newLeft) * (newTop - newBottom);
        return newArea - area();
    }

    public float area() {
        return (right - left) * (top - bottom);
    }

    public boolean intersecc(MBR rect) {
        boolean interX = (left <= rect.left && rect.left <= right) || (left <= rect.right && rect.right <= right);
        boolean interY = (bottom <= rect.bottom && rect.bottom <= top) || (bottom <= rect.top && rect.top <= top);
        return interX && interY;
    }

    public float getLeft() {
        return left;
    }

    public float getRight() {
        return right;
    }

    public float getBottom() {
        return bottom;
    }

    public float getTop() {
        return top;
    }

    @Override
    public String toString() {
        return "p1: (" + left + ", " + bottom + ") p2: (" + right + ", " + top + ")";
    }
}
