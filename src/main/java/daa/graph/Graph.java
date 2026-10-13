package daa.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Adjacency-list graph supporting directed and undirected edges. */
public final class Graph {
    private final int n;
    private final boolean directed;
    private final List<List<Edge>> adjacency;
    private final List<Edge> edges;

    public Graph(int n, boolean directed) {
        if (n < 0) throw new IllegalArgumentException("n cannot be negative");
        this.n = n; this.directed = directed;
        adjacency = new ArrayList<>(n);
        for (int i = 0; i < n; i++) adjacency.add(new ArrayList<>());
        edges = new ArrayList<>();
    }
    public int size() { return n; }
    public boolean isDirected() { return directed; }
    public List<Edge> neighbors(int v) { checkVertex(v); return Collections.unmodifiableList(adjacency.get(v)); }
    public List<Edge> edges() { return Collections.unmodifiableList(edges); }
    public int edgeCount() { return edges.size(); }
    public void addEdge(int u, int v, int w) {
        checkVertex(u); checkVertex(v);
        Edge e = new Edge(u, v, w); edges.add(e); adjacency.get(u).add(e);
        if (!directed && u != v) adjacency.get(v).add(new Edge(v, u, w));
    }
    public Graph reversed() {
        Graph r = new Graph(n, true);
        for (Edge e : edges) r.addEdge(e.to(), e.from(), e.weight());
        return r;
    }
    private void checkVertex(int v) { if (v < 0 || v >= n) throw new IndexOutOfBoundsException("Invalid vertex: " + v); }
}
