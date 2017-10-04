package RTree;

public class LinearRTree extends RTree {

    static void experiment(int rekts, long seed) {
        System.out.println("Launching experiment for Linear with n = " + rekts);

        Experiment.reset(seed);
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

    public LinearRTree(IRTreeNode node1, IRTreeNode node2) {
        root.addLocally(node1);
        root.addLocally(node2);
    }

    @Override
    public IRTreeNode newNode() {
        return new LinearNode();
    }
}
