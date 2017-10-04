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

    void update(MBR rekt) {
        left = Math.min(left, rekt.left);
        right = Math.max(right, rekt.right);
        bottom = Math.min(bottom, rekt.bottom);
        top = Math.max(top, rekt.top);
    }

    float calcChange(MBR rekt) {
        float newLeft = Math.min(left, rekt.left);
        float newRight = Math.max(right, rekt.right);
        float newBottom = Math.min(bottom, rekt.bottom);
        float newTop = Math.max(top, rekt.top);

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
        return "mbr(" + left + ", " + bottom + "):(" + right + ", " + top + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == MBR.class) {
            MBR o = (MBR) obj;
            return o.left == left && o.right == right && o.bottom == bottom && o.top == top;
        }
        return false;
    }
}
