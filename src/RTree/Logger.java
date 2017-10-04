package RTree;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class Logger {

    public static void log(String s) {
        byte data[] = s.getBytes();
        Path p = Paths.get("./logfile.txt");

        try (OutputStream out = new BufferedOutputStream(
                Files.newOutputStream(p, CREATE, APPEND))) {
            out.write(data, 0, data.length);
        } catch (IOException x) {
            System.err.println(x);
        }
    }

    public static void log(String tree, int rekts, double insertion_time, double search_time, int insertion_reads,
                           int insertion_writes, int search_reads, int search_writes, float v) {
        String s = tree + ": " + System.lineSeparator() + "\t" + "Se insertaron: " + rekts + " rectangulos" +
                System.lineSeparator() + "\t" + "Tiempo de inserción: " + insertion_time + " segundos" +
                System.lineSeparator() + "\t" + "Tiempo de búsqueda: " + search_time + " segundos" +
                System.lineSeparator() + "\t" + "Lecturas a disco durante insertion: " + insertion_reads +
                System.lineSeparator() + "\t" + "Escrituras a disco durante insertion: " + insertion_writes +
                System.lineSeparator() + "\t" + "Lecturas a disco durante search: " + search_reads +
                System.lineSeparator() + "\t" + "Escrituras a disco durante search: " + search_writes +
                System.lineSeparator() + "\t" + "Uso por bloque: " + v + "%" + System.lineSeparator() + System.lineSeparator();
        log(s);
    }
}
