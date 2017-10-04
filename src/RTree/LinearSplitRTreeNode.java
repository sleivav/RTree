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
        if (normalizedX >= normalizedY) {
            initialNodeA.addLocally(getChild(maxLeftIndex));
            //initialNodeA = getChild(maxLeftIndex);
            initialNodeB.addLocally(getChild(minRightIndex));
            //initialNodeB = getChild(minRightIndex);
            children.remove(maxLeftIndex);
            children.remove(minRightIndex);
        } else {
            initialNodeA.addLocally(getChild(maxBottomIndex));
            //initialNodeA = getChild(maxBottomIndex);
            initialNodeB.addLocally(getChild(minTopIndex));
            //initialNodeB = getChild(minTopIndex);
            children.remove(maxBottomIndex);
            children.remove(minTopIndex);
        }
        Collections.shuffle(children);
        for (int i = 0; i < children.size(); i++) {
            IRTreeNode child = getChild(i);
            MBR rektA = initialNodeA.getRectangle();
            MBR rektB = initialNodeB.getRectangle();
            if (rektA.calcChange(child) > rektB.calcChange(child) || (children.size() - i < getMin() &&
                                                                      initialNodeA.size() < getMin())) {
                initialNodeB.add(children.get(i));
                rektB.update(child);
            } else {
                initialNodeA.add(children.get(i));
                rektA.update(child);
            }
        }
        return new IRTreeNode[]{initialNodeA, initialNodeB};
    }
}
