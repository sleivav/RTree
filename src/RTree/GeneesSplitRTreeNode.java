package RTree;


import java.util.ArrayList;

public class GeneesSplitRTreeNode extends AbstractRTreeNode {

    @Override
    public IRTreeNode split() {
        // Variables para determinar las separaciones
        double maxLeft = Double.MIN_VALUE;
        int maxLeftIndex = 0;
        double maxBottom = Double.MIN_VALUE;
        int maxBottomIndex = 0;
        double minRight = Double.MAX_VALUE;
        int minRightIndex = 0;
        double minTop = Double.MAX_VALUE;
        int minTopIndex = 0;

        // Variables para determinar la normalizacion
        double minLeft = Double.MAX_VALUE;
        double minBottom = Double.MAX_VALUE;
        double maxRight = Double.MIN_VALUE;
        double maxTop = Double.MIN_VALUE;

        for (int i = 0; i < size(); i++) {
            MBR rekt = this.getChild(i).getRectangle();
            if (rekt.getLeft() > maxLeft) {
                maxLeft = rekt.getLeft();
                maxLeftIndex = i;
            }
            if (rekt.getLeft() < minLeft) {
                minLeft = rekt.getLeft();
            }
            if (rekt.getBottom() > maxBottom) {
                maxBottom = rekt.getBottom();
                maxBottomIndex = i;
            }
            if (rekt.getBottom() < minBottom) {
                minBottom = rekt.getBottom();
            }
            if (rekt.getRight() < minRight) {
                minRight = rekt.getRight();
                minRightIndex = i;
            }
            if (rekt.getRight() > maxRight) {
                maxRight = rekt.getRight();
            }
            if (rekt.getTop() < minTop) {
                minTop = rekt.getTop();
                minTopIndex = i;
            }
            if (rekt.getTop() > maxTop) {
                maxTop = rekt.getTop();
            }
        }

        double separationX = Math.abs(maxLeft - minRight);
        double separationY = Math.abs(maxBottom - minTop);
        double rangeX = maxRight - minLeft;
        double rangeY = maxTop - minBottom;
        double normalizedX = separationX / rangeX;
        double normalizedY = separationY / rangeY;

        IRTreeNode initialNodeA;
        IRTreeNode initialNodeB;
        ArrayList<Long> children = this.getChildren();
        if (normalizedX >= normalizedY) {
            initialNodeA = getChild(maxLeftIndex);
            initialNodeB = getChild(minRightIndex);
            children.remove(maxLeftIndex);
            children.remove(minRightIndex);
        } else {
            initialNodeA = getChild(maxBottomIndex);
            initialNodeB = getChild(minTopIndex);
            children.remove(maxBottomIndex);
            children.remove(minTopIndex);
        }

        // TODO ordenar en dimensión y asignar mitad y mitad

        return null;
    }
}
