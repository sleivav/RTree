package RTree;

class GreenesRTree extends RTree {
    static void experiment(int rekts, long seed) {
        System.out.println("Launching experiment for Greenes with n = " + rekts);

        Experiment.reset(seed);
        long t0 = System.currentTimeMillis();

        GreenesRTree tree = new GreenesRTree(Experiment.getRekt(), Experiment.getRekt());

        // inserciones
        for (int i = 0; i < rekts; i++) {
            MBR rekt = Experiment.getRekt();
            tree.add(rekt);
            if (i % (rekts / 5) == 0 && i > 0) {
                System.out.println(i + " insertions done...");
            }
        }
        System.out.println("insertions ended.");

        // busquedas
        for (int i = 0; i < rekts / 10; i++) {
            MBR rekt = Experiment.getRekt();
            tree.search(rekt);
            if (i % (rekts / 50) == 0 && i > 0) {
                System.out.println(i + " searches done...");
            }
        }
        System.out.println("searches ended.");

        System.out.println((System.currentTimeMillis() - t0) / 1000 + " seconds");
        System.out.println(tree.blockUsage() + "% block usage");
    }

    private GreenesRTree(MBR rekt1, MBR rekt2) {
        root = new GreenesSplitRTreeNode(rekt1);
        add(rekt2);
    }

    public IRTreeNode newNode() {
        return new GreenesSplitRTreeNode();
    }
}
