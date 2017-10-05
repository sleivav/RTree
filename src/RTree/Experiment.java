package RTree;

import java.io.File;
import java.util.Random;

public class Experiment {
    private static Random rand = new Random();

    public static void main(String[] args) {
        int n = 10;
        long t = System.currentTimeMillis();
        experiment(new LinearRTree(), n, t);
        experiment(new GreenesRTree(), n, t);
    }

    private static void experiment(RTree tree, int n, long seed) {
	int rekts = (int) Math.pow(2, n);
        String className = tree.getClass().getSimpleName();
        System.out.println("Launching experiment for " + className + " with n = " + rekts);

        reset(seed);
        RTreeNode.reset();
        long t0 = System.currentTimeMillis();

        // inserciones
        for (int i = 0; i < rekts; i++) {
            tree.add(getRekt());
            if (i % (rekts / 5) == 0 && i > 0) {
                System.out.println(i + " insertions done...");
            }
        }
        long tf = System.currentTimeMillis();
        double insertion_time = (tf - t0) / 1000.0;
        System.out.println("insertions ended");
        System.out.println("  took " + (tf - t0) / 1000 + " seconds.\n");
        t0 = System.currentTimeMillis();

        int insertion_reads = tree.root.getReads();
        int insertion_writes = tree.root.getWrites();
        RTreeNode.reset();

        // busquedas
        for (int i = 0; i < rekts / 10; i++) {
            tree.search(getRekt());
            if (i % (rekts / 50) == 0 && i > 0) {
                System.out.println(i + " searches done...");
            }
        }
        System.out.println("searches ended.");
        double search_time = (System.currentTimeMillis() - t0) / 1000.0;
        System.out.println("  took " + (System.currentTimeMillis() - t0) / 1000 + " seconds");
        System.out.println("  read " + tree.root.getReads() + " times.\n");

        System.out.println(tree.blockUsage() + "% block usage\n");
        Logger.log(className, rekts, insertion_time, search_time, insertion_reads, insertion_writes,
                tree.root.getReads(), tree.root.spaceUsed(), tree.blockUsage());
    }

    private static MBR getRekt() {
        float x = rand.nextFloat() * 500000;
        float y = rand.nextFloat() * 500000;

        float width = rand.nextFloat() * 100;
        float height = rand.nextFloat() * 100;

        return new MBR(x, y, x + width, y + height);
    }

    private static void reset(long seed) {
        rand = new Random(seed);
    }
}
