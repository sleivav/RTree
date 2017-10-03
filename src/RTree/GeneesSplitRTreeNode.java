package RTree;


import java.util.ArrayList;

public class GeneesSplitRTreeNode extends AbstractRTreeNode {

    @Override
    public IRTreeNode[] split() {
        // Variables para determinar las separaciones
        double maxLeft = Double.MIN_VALUE;
        double maxBottom = Double.MIN_VALUE;
        double minRight = Double.MAX_VALUE;
        double minTop = Double.MAX_VALUE;

        // Variables para determinar la normalizacion
        double minLeft = Double.MAX_VALUE;
        double minBottom = Double.MAX_VALUE;
        double maxRight = Double.MIN_VALUE;
        double maxTop = Double.MIN_VALUE;

        // cargar hijos
        IRTreeNode[] nodes = new IRTreeNode[size()];
        for (int i = 0; i < size(); i++) {
            nodes[i] = getChild(i);
        }

        for (int i = 0; i < size(); i++) {
            MBR rekt = nodes[i].getRectangle();

            minLeft = Math.min(minLeft, rekt.getLeft());
            maxLeft = Math.max(maxLeft, rekt.getLeft());

            minBottom = Math.min(minBottom, rekt.getBottom());
            maxBottom = Math.max(maxBottom, rekt.getBottom());

            minRight = Math.min(minRight, rekt.getRight());
            maxRight = Math.max(maxRight, rekt.getRight());

            minTop = Math.min(minTop, rekt.getTop());
            maxTop = Math.max(maxTop, rekt.getTop());
        }

        double separationX = Math.abs(maxLeft - minRight);
        double separationY = Math.abs(maxBottom - minTop);
        double rangeX = maxRight - minLeft;
        double rangeY = maxTop - minBottom;
        double normalizedX = separationX / rangeX;
        double normalizedY = separationY / rangeY;

        int dimension;
        if (normalizedX >= normalizedY) {
            dimension = 0;
        } else {
            dimension = 1;
        }

        ArrayList<Long> children = bubblesort(dimension, nodes);
        IRTreeNode node1 = new GeneesSplitRTreeNode();
        IRTreeNode node2 = new GeneesSplitRTreeNode();

        int mid = children.size() / 2;
        for (int i = 0; i < mid; i++) {
            node1.addLocally(nodes[i]);
            node2.addLocally(nodes[i + mid]);
        }
        if (children.size() % 2 == 1)
            node2.addLocally(nodes[2 * mid]);

        node1.writeToDisk();
        node2.writeToDisk();

        this.deleteFromDisk();

        return new IRTreeNode[]{node1, node2};
    }

    private ArrayList<Long> bubblesort(int dim, IRTreeNode[] nodes) {
        // cargar todos los hijos para sort
        ArrayList<Long> children = getChildren();

        // bubblesort best sort
        for (int i = 0; i < size(); i++) {
            for (int j = 1; j < size(); j++) {
                if (lessThan(nodes[i], nodes[i - 1], dim)) {
                    swap(i, i - 1, children, nodes);
                }
            }
        }

        return children;
    }

    private void swap(int i, int j, ArrayList<Long> children, IRTreeNode[] nodes) {
        Long id = children.get(i);
        IRTreeNode node = nodes[i];
        children.set(i, children.get(j));
        nodes[i] = nodes[j];
        children.set(j, id);
        nodes[j] = node;
    }

    private boolean lessThan(IRTreeNode node1, IRTreeNode node2, int dim) {
        if (dim == 0)
            return node1.getRectangle().getLeft() < node2.getRectangle().getLeft();
        return node1.getRectangle().getBottom() < node2.getRectangle().getBottom();
    }
}
