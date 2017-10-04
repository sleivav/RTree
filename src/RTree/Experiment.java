package RTree;

import java.util.Random;

public class Experiment {
    private static Random rand;

    public static void main(String[] args) {
        GreenesRTree.experiment(1000, 1);
    }

    static MBR getRekt() {
        float x = rand.nextInt(10);
        float y = rand.nextInt(10);

        float width = rand.nextInt(10);
        float height = rand.nextInt(10);

        return new MBR(x, y, x + width, y + height);
    }

    static void reset(long seed) {
        rand = new Random(seed);
    }
}
