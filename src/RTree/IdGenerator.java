package RTree;

import java.io.*;

public class IdGenerator {

    public static final File FILE = new File(RTree.DIR + "id");

    public static Long nextId() {
        try {
            Long res;
            if (FILE.exists()) {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE));
                res = in.readLong();
            } else {
                res = Long.valueOf(0);
            }
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE));
            out.writeLong(res + 1);
            out.close();
            return res;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
            return Long.valueOf(0);
        }
    }
}
