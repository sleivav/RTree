package RTree;

import java.util.ArrayList;

public class GreenesRTree extends RTree {
    public static void main(String[] args) {
        IRTreeNode node1 = new GreenesSplitRTreeNode(getRekt());
        IRTreeNode node2 = new GreenesSplitRTreeNode(getRekt());
        node1.writeToDisk();
        node2.writeToDisk();

        GreenesRTree tree = new GreenesRTree(node1, node2);

        long t0 = System.currentTimeMillis();

        int rekts = 1000;
        for (int i = 0; i < rekts; i++) {
            IRTreeNode node = new GreenesSplitRTreeNode(getRekt());
            node.writeToDisk();
            tree.add(node.getId());
            if (i % 100 == 0) {
                System.out.println(i);
            }
        }
        System.out.println((System.currentTimeMillis() - t0)/1000 + " seconds");

        System.out.println("busqueda 1:");
        System.out.println(node1.getRectangle() + "\n");
        ArrayList<MBR> found = tree.search(node1.getRectangle());
        found.forEach(System.out::println);

        System.out.println("\nbusqueda 2:");
        System.out.println(node2.getRectangle() + "\n");
        found = tree.search(node2.getRectangle());
        found.forEach(System.out::println);
    }

    public GreenesRTree(IRTreeNode node1, IRTreeNode node2) {
        root.addLocally(node1);
        root.addLocally(node2);
    }

    public IRTreeNode newNode() {
        return new GreenesSplitRTreeNode();
    }
}
