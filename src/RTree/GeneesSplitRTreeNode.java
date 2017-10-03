package RTree;


import java.util.ArrayList;

public class GeneesSplitRTreeNode extends AbstractRTreeNode {

    @Override
    public IRTreeNode split() {
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

        for (int i = 0; i < size(); i++) {
            MBR rekt = this.getChild(i).getRectangle();
            if (rekt.getLeft() > maxLeft) {
                maxLeft = rekt.getLeft();
            }
            if (rekt.getLeft() < minLeft) {
                minLeft = rekt.getLeft();
            }
            if (rekt.getBottom() > maxBottom) {
                maxBottom = rekt.getBottom();
            }
            if (rekt.getBottom() < minBottom) {
                minBottom = rekt.getBottom();
            }
            if (rekt.getRight() < minRight) {
                minRight = rekt.getRight();
            }
            if (rekt.getRight() > maxRight) {
                maxRight = rekt.getRight();
            }
            if (rekt.getTop() < minTop) {
                minTop = rekt.getTop();
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

        int dimension;
        if (normalizedX >= normalizedY) {
            dimension = 0;
        } else {
            dimension = 1;
        }

        // TODO ordenar en dimensión y asignar mitad y mitad

        ArrayList<Long> children = bubblesort(dimension);
        ArrayList<Long> children1 = new ArrayList<Long>();
        ArrayList<Long> children2 = new ArrayList<Long>();

        int mid = children.size() / 2;
        for (int i = 0; i < mid; i++) {
            children1.add(children.get(i));
            children2.add(children.get(i + mid));
        }
        if (children.size() % 2 == 1)
            children2.add(children.get(2 * mid - 1));

        IRTreeNode node1 = new GeneesSplitRTreeNode();
        node1.setChildren(children1);

        IRTreeNode node2 = new GeneesSplitRTreeNode();
        node2.setChildren(children2);

        // TODO crear padre

        return null;
    }

    private ArrayList<Long> bubblesort(int dim) {
        // cargar todos los hijos para sort
        ArrayList<Long> children = getChildren();
        IRTreeNode[] nodes = new IRTreeNode[size()];
        for (int i = 0; i < size(); i++) {
            nodes[i] = getChild(i);
        }

        // bubblesort best sort
        for (int i = 0; i < size(); i++) {
            for (int j = i + 1; j < size(); j++) {
                if (lessThan(nodes[i], nodes[i - 1], dim)) {
                    swap(i, i - 1, children, nodes);
                }
            }
        }

        setChildren(children);
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
