package RTree;

import java.util.Random;

public class Experiment {
    private static Random rand;

    public static void main(String[] args) {
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
}
