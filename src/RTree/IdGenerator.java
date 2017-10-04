package RTree;

import java.io.*;

class IdGenerator {

    private static final File FILE = new File(RTree.DIR + "id");

    static Long nextId() {
        try {
            Long res;
            if (FILE.exists()) {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE));
                res = in.readLong();
            } else {
                res = 0L;
            }
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE));
            out.writeLong(res + 1);
            out.close();
            return res;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
            return 0L;
        }
    }
}
