package daa.graph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

/** Reads: n m directed|undirected followed by m lines u v w. */
public final class GraphReader {
    private GraphReader() { }
    public static Graph read(Path path) throws IOException {
        try (Scanner sc = new Scanner(Files.newBufferedReader(path))) {
            if (!sc.hasNextInt()) throw new IOException("Missing vertex count");
            int n = sc.nextInt(), m = sc.nextInt(); String type = sc.next();
            boolean directed = switch (type.toLowerCase()) { case "directed" -> true; case "undirected" -> false; default -> throw new IOException("Expected directed or undirected"); };
            Graph g = new Graph(n, directed);
            for (int i = 0; i < m; i++) {
                if (!sc.hasNextInt()) throw new IOException("Missing edge at index " + i);
                int u = sc.nextInt(), v = sc.nextInt(), w = sc.nextInt(); g.addEdge(u, v, w);
            }
            return g;
        }
    }
}
