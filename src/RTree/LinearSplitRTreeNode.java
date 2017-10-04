package RTree;

import java.util.ArrayList;
import java.util.Collections;

public class LinearSplitRTreeNode extends AbstractRTreeNode {
    public LinearSplitRTreeNode() {
        super();
    }

    public LinearSplitRTreeNode(MBR rekt) {
        super(rekt);
    }

    @Override
    public IRTreeNode[] split() {
        ArrayList<IRTreeNode> nodes = new ArrayList<IRTreeNode>();
        // Variables para determinar las separaciones
        float maxLeft = Float.MIN_VALUE;
        int maxLeftIndex = 0;
        float maxBottom = Float.MIN_VALUE;
        int maxBottomIndex = 0;
        float minRight = Float.MAX_VALUE;
        int minRightIndex = 0;
        float minTop = Float.MAX_VALUE;
        int minTopIndex = 0;

        // Variables para determinar la normalizacion
        float minLeft = Float.MAX_VALUE;
        float minBottom = Float.MAX_VALUE;
        float maxRight = Float.MIN_VALUE;
        float maxTop = Float.MIN_VALUE;

        for (int i = 0; i < size(); i++) {
            nodes.add(this.getChild(i));
            MBR rekt = nodes.get(i).getRectangle();
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

        float separationX = Math.abs(maxLeft - minRight);
        float separationY = Math.abs(maxBottom - minTop);
        float rangeX = maxRight - minLeft;
        float rangeY = maxTop - minBottom;
        float normalizedX = separationX / rangeX;
        float normalizedY = separationY / rangeY;

        IRTreeNode initialNodeA;
        IRTreeNode initialNodeB;
        ArrayList<Long> children = this.getChildren();
        initialNodeA = new LinearSplitRTreeNode();
        initialNodeB = new LinearSplitRTreeNode();
        int lowIndex;
        int highIndex;
        if (normalizedX >= normalizedY) {
            lowIndex = maxLeftIndex;
            highIndex = minRightIndex;
        } else {
            lowIndex = maxBottomIndex;
            highIndex = minTopIndex;
        }
        initialNodeA.addLocally(getChild(lowIndex));
        initialNodeB.addLocally(getChild(highIndex));
        children.remove(lowIndex);
        children.remove(highIndex);
        getData().remove(lowIndex);
        getData().remove(highIndex);

        Collections.shuffle(children);
        for (int i = 0; i < children.size(); i++) {
            IRTreeNode child = getChild(i);
            MBR rektA = initialNodeA.getRectangle();
            MBR rektB = initialNodeB.getRectangle();
            MBR dataRec = getData().get(i);
            if (rektA.calcChange(dataRec) > rektB.calcChange(dataRec)
                    || (children.size() - i < getMin() && initialNodeA.size() < getMin())) {
                initialNodeB.addLocally(child);
                rektB.update(dataRec);
            } else {
                initialNodeA.addLocally(child);
                rektA.update(dataRec);
            }
        }
        return new IRTreeNode[]{initialNodeA, initialNodeB};
    }

    public IRTreeNode[] splitLeaf() {
        ArrayList<MBR> rectangles = new ArrayList<MBR>();
        // Variables para determinar las separaciones
        float maxLeft = Float.MIN_VALUE;
        int maxLeftIndex = 0;
        float maxBottom = Float.MIN_VALUE;
        int maxBottomIndex = 0;
        float minRight = Float.MAX_VALUE;
        int minRightIndex = 0;
        float minTop = Float.MAX_VALUE;
        int minTopIndex = 0;

        // Variables para determinar la normalizacion
        float minLeft = Float.MAX_VALUE;
        float minBottom = Float.MAX_VALUE;
        float maxRight = Float.MIN_VALUE;
        float maxTop = Float.MIN_VALUE;

        for (int i = 0; i < size(); i++) {
            rectangles.add(getData().get(i));
            MBR rekt = rectangles.get(i);
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

        float separationX = Math.abs(maxLeft - minRight);
        float separationY = Math.abs(maxBottom - minTop);
        float rangeX = maxRight - minLeft;
        float rangeY = maxTop - minBottom;
        float normalizedX = separationX / rangeX;
        float normalizedY = separationY / rangeY;

        IRTreeNode initialNodeA;
        IRTreeNode initialNodeB;
        ArrayList<Long> children = this.getChildren();
        initialNodeA = new LinearSplitRTreeNode();
        initialNodeB = new LinearSplitRTreeNode();
        int lowIndex;
        int highIndex;
        if (normalizedX >= normalizedY) {
            lowIndex = maxLeftIndex;
            highIndex = minRightIndex;
        } else {
            lowIndex = maxBottomIndex;
            highIndex = minTopIndex;
        }
        initialNodeA.getData().add(rectangles.get(lowIndex));
        initialNodeB.getData().add(rectangles.get(highIndex));
        rectangles.remove(lowIndex);
        rectangles.remove(highIndex);
        getData().remove(lowIndex);
        getData().remove(highIndex);

        Collections.shuffle(rectangles);
        for (int i = 0; i < rectangles.size(); i++) {
            IRTreeNode child = getChild(i);
            MBR rektA = initialNodeA.getRectangle();
            MBR rektB = initialNodeB.getRectangle();
            MBR dataRec = getData().get(i);
            if (rektA.calcChange(dataRec) > rektB.calcChange(dataRec)
                    || (rectangles.size() - i < getMin() && initialNodeA.size() < getMin())) {
                initialNodeB.getData().add(dataRec);
                rektB.update(dataRec);
            } else {
                initialNodeA.getData().add(dataRec);
                rektA.update(dataRec);
            }
        }
        return new IRTreeNode[]{initialNodeA, initialNodeB};
    }
}
