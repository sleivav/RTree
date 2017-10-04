package RTree;

import java.io.Serializable;

class MBR implements Serializable {
    private float left, bottom, right, top;

    MBR(float left, float bottom, float right, float top) {
        this.left = left;
        this.bottom = bottom;
        this.right = right;
        this.top = top;
    }

    MBR copy() {
        return new MBR(left, bottom, right, top);
    }

    void update(IRTreeNode node) {
        left = Math.min(left, node.getRectangle().left);
        right = Math.max(right, node.getRectangle().right);
        bottom = Math.min(bottom, node.getRectangle().bottom);
        top = Math.max(top, node.getRectangle().top);
    }

    float calcChange(IRTreeNode node) {
        float newLeft = Math.min(left, node.getRectangle().left);
        float newRight = Math.max(right, node.getRectangle().right);
        float newBottom = Math.min(bottom, node.getRectangle().bottom);
        float newTop = Math.max(top, node.getRectangle().top);

        float newArea = (newRight - newLeft) * (newTop - newBottom);
        return newArea - area();
    }

    float area() {
        return (right - left) * (top - bottom);
    }

    boolean intersecc(MBR rect) {
        boolean interX = (left <= rect.left && rect.left <= right) || (left <= rect.right && rect.right <= right);
        boolean interY = (bottom <= rect.bottom && rect.bottom <= top) || (bottom <= rect.top && rect.top <= top);
        return interX && interY;
    }

    float getLeft() {
        return left;
    }

    float getRight() {
        return right;
    }

    float getBottom() {
        return bottom;
    }

    float getTop() {
        return top;
    }

    @Override
    public String toString() {
        return "p1: (" + left + ", " + bottom + ") p2: (" + right + ", " + top + ")";
    }
}
