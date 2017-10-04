package RTree;

import java.io.File;
import java.util.Random;

public class Experiment {
    private static Random rand = new Random();

    public static void main(String[] args) {
        GreenesRTree.experiment(10000, 1);
        GreenesRTree.experiment(10000, 1);
    }

    static MBR getRekt() {
        float x = rand.nextFloat() * 500000;
        float y = rand.nextFloat() * 500000;

        float width = rand.nextFloat() * 100;
        float height = rand.nextFloat() * 100;

        return new MBR(x, y, x + width, y + height);
    }

    static void reset(long seed) {
        rand = new Random(seed);
    }

    static int getM() {
        GreenesNode node = new GreenesNode();
        int m = 0;

        for (int i = 0; i < 200; i++) {
            node.getData().add(getRekt());
            node.getChildren().add(rand.nextLong());
            node.writeToDisk();

            File f = new File(RTree.DIR + "r" + node.getId() + ".node");
            if (f.length() > 4096) break;
            m = i;
        }
        return m;
    }
}
