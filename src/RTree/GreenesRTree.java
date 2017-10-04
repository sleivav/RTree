package RTree;

class GreenesRTree extends RTree {
    static void experiment(int rekts, long seed) {
        System.out.println("Launching experiment for Greenes with n = " + rekts);

        Experiment.reset(seed);
        long t0 = System.currentTimeMillis();

        IRTreeNode node1 = new GreenesSplitRTreeNode(Experiment.getRekt());
        IRTreeNode node2 = new GreenesSplitRTreeNode(Experiment.getRekt());
        node1.writeToDisk();
        node2.writeToDisk();

        GreenesRTree tree = new GreenesRTree(node1, node2);

        // inserciones
        for (int i = 0; i < rekts; i++) {
            IRTreeNode node = new GreenesSplitRTreeNode(Experiment.getRekt());
            node.writeToDisk();
            tree.add(node.getId());
            if (i % (rekts / 20) == 0 && i > 0) {
                System.out.println(i + " insertions done...");
            }
        }
        System.out.println("insertions ended.");

        // busquedas
        for (int i = 0; i < rekts / 10; i++) {
            MBR rekt = Experiment.getRekt();
            tree.search(rekt);
            if (i % (rekts / 200) == 0 && i > 0) {
                System.out.println(i + " searches done...");
            }
        }
        System.out.println("searches ended.");

        System.out.println((System.currentTimeMillis() - t0) / 1000 + " seconds");
        System.out.println(tree.blockUsage() + "% block usage");
    }

    GreenesRTree(IRTreeNode node1, IRTreeNode node2) {
        root.addLocally(node1);
        root.addLocally(node2);
    }

    public IRTreeNode newNode() {
        return new GreenesSplitRTreeNode();
    }
}
