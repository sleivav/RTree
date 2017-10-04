package RTree;

class LinearRTree extends RTree {

    static void experiment(int rekts, long seed) {
        // TODO guardar n, block usage, writes y reads a log.
        System.out.println("Launching experiment for Linear with n = " + rekts);

        Experiment.reset(seed);
        RTreeNode.reset();
        long t0 = System.currentTimeMillis();
        IRTreeNode node1 = new LinearNode(Experiment.getRekt());
        IRTreeNode node2 = new LinearNode(Experiment.getRekt());
        node1.writeToDisk();
        node2.writeToDisk();

        LinearRTree tree = new LinearRTree(node1, node2);

        // inserciones
        for (int i = 0; i < rekts; i++) {
            MBR rekt = Experiment.getRekt();
            tree.add(rekt);
            if (i % (rekts / 5) == 0 && i > 0) {
                System.out.println(i + " insertions done...");
            }
        }
        System.out.println("insertions ended");
        System.out.println("  took " + (System.currentTimeMillis() - t0) / 1000 + " seconds.\n");
        t0 = System.currentTimeMillis();

        // busquedas
        for (int i = 0; i < rekts / 10; i++) {
            MBR rekt = Experiment.getRekt();
            tree.search(rekt);
            if (i % (rekts / 50) == 0 && i > 0) {
                System.out.println(i + " searches done...");
            }
        }
        System.out.println("searches ended.");

        System.out.println("  took " + (System.currentTimeMillis() - t0) / 1000 + " seconds.\n");

        System.out.println("read " + tree.root.getReads() + " times");
        System.out.println("wrote " + tree.root.getWrites() + " times");

        System.out.println(tree.blockUsage() + "% block usage\n");
    }

    private LinearRTree(IRTreeNode node1, IRTreeNode node2) {
        root.addLocally(node1);
        root.addLocally(node2);
    }

    @Override
    public IRTreeNode newNode() {
        return new LinearNode();
    }
}
