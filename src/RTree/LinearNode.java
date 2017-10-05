package RTree;

import java.util.ArrayList;
import java.util.Collections;

public class LinearNode extends RTreeNode {
    LinearNode() {
        super();
    }

    LinearNode(MBR rekt) {
        super(rekt);
    }

    @Override
    public IRTreeNode[] split() {
        if(isLeaf()) {
            return splitLeaf();
        }
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
        initialNodeA = new LinearNode();
        initialNodeB = new LinearNode();
        int lowIndex;
        int highIndex;
        if (normalizedX >= normalizedY) {
            lowIndex = maxLeftIndex;
            highIndex = minRightIndex;
        } else {
            lowIndex = maxBottomIndex;
            highIndex = minTopIndex;
        }
        initialNodeA.addLocally(nodes.get(lowIndex));
        initialNodeB.addLocally(nodes.get(highIndex));
        nodes.remove(lowIndex);
        getData().remove(lowIndex);
        if (lowIndex < highIndex) {
            nodes.remove(highIndex - 1);
            getData().remove(highIndex - 1);
        } else {
            nodes.remove(highIndex);
            getData().remove(highIndex);
        }

        Collections.shuffle(nodes);
        for (int i = 0; i < nodes.size(); i++) {
            IRTreeNode child = nodes.get(i);
            MBR rektA = initialNodeA.getRectangle();
            MBR rektB = initialNodeB.getRectangle();
            MBR dataRec = nodes.get(i).getRectangle();
            if (rektA.calcChange(dataRec) > rektB.calcChange(dataRec)
                    || (nodes.size() - i < getMin() && initialNodeA.size() < getMin())) {
                initialNodeB.addLocally(child);
            } else {
                initialNodeA.addLocally(child);
            }
        }
        initialNodeA.writeToDisk();
        initialNodeB.writeToDisk();
        this.deleteFromDisk();
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
        int lowIndex;
        int highIndex;
        if (normalizedX >= normalizedY) {
            lowIndex = maxLeftIndex;
            highIndex = minRightIndex;
        } else {
            lowIndex = maxBottomIndex;
            highIndex = minTopIndex;
        }
        initialNodeA = new LinearNode(rectangles.get(lowIndex));
        initialNodeB = new LinearNode(rectangles.get(highIndex));
        rectangles.remove(lowIndex);
        getData().remove(lowIndex);
        if (lowIndex < highIndex) {
            rectangles.remove(highIndex - 1);
            getData().remove(highIndex - 1);
        } else {
            rectangles.remove(highIndex);
            getData().remove(highIndex);
        }

        Collections.shuffle(rectangles);
        for (int i = 0; i < rectangles.size(); i++) {
            MBR rektA = initialNodeA.getRectangle();
            MBR rektB = initialNodeB.getRectangle();
            MBR dataRec = rectangles.get(i);
            if (rektA.calcChange(dataRec) > rektB.calcChange(dataRec)
                    || (rectangles.size() - i < getMin() && initialNodeA.size() < getMin())) {
                initialNodeB.getData().add(dataRec);
            } else {
                initialNodeA.getData().add(dataRec);
            }
        }
        initialNodeA.writeToDisk();
        initialNodeB.writeToDisk();
        this.deleteFromDisk();
        return new IRTreeNode[]{initialNodeA, initialNodeB};
    }
}
