package RTree;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

class Logger {

    static private void log(String s) {
        byte data[] = s.getBytes();
        Path p = Paths.get("./logfile.csv");

        try (OutputStream out = new BufferedOutputStream(
                Files.newOutputStream(p, CREATE, APPEND))) {
            out.write(data, 0, data.length);
        } catch (IOException x) {
            System.err.println(x.toString());
        }
    }

    static void log(String tree, int rekts, double insertionTime, double searchTime, int insertionReads,
                           int insertionWrites, int searchReads, long spaceUsed, float usage) {
        String s = tree + ", " + rekts + ", " + insertionTime + ", " + searchTime + ", " +
                insertionReads + ", " + insertionWrites + ", " + searchReads + ", " + spaceUsed + 
		", " +  usage + System.lineSeparator();
        log(s);
    }
}
