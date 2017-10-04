package RTree;


import java.util.ArrayList;

class GreenesSplitRTreeNode extends AbstractRTreeNode {

    GreenesSplitRTreeNode() {
        super();
    }

    GreenesSplitRTreeNode(MBR rekt) {
        super(rekt);
    }

    @Override
    public IRTreeNode[] split() {
        // Variables para determinar las separaciones
        float maxLeft = Float.MIN_VALUE;
        float maxBottom = Float.MIN_VALUE;
        float minRight = Float.MAX_VALUE;
        float minTop = Float.MAX_VALUE;

        // Variables para determinar la normalizacion
        float minLeft = Float.MAX_VALUE;
        float minBottom = Float.MAX_VALUE;
        float maxRight = Float.MIN_VALUE;
        float maxTop = Float.MIN_VALUE;

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

        float separationX = Math.abs(maxLeft - minRight);
        float separationY = Math.abs(maxBottom - minTop);
        float rangeX = maxRight - minLeft;
        float rangeY = maxTop - minBottom;
        float normalizedX = separationX / rangeX;
        float normalizedY = separationY / rangeY;

        int dimension;
        if (normalizedX >= normalizedY) {
            dimension = 0;
        } else {
            dimension = 1;
        }

        ArrayList<Long> children = bubblesort(dimension, nodes);
        IRTreeNode node1 = new GreenesSplitRTreeNode();
        IRTreeNode node2 = new GreenesSplitRTreeNode();

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
                if (lessThan(nodes[j], nodes[j - 1], dim)) {
                    swap(j, j - 1, children, nodes);
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
