package daa.algorithms;

import daa.graph.Edge;
import daa.graph.Graph;
import java.util.*;

public final class MstAlgorithms {
    private MstAlgorithms() { }
    public static MstResult kruskal(Graph g) {
        if (g.isDirected()) throw new IllegalArgumentException("MST requires an undirected graph");
        Metrics m = new Metrics(); List<Edge> sorted = new ArrayList<>(g.edges());
        sorted.sort((a,b) -> { m.comparisons++; return Integer.compare(a.weight(), b.weight()); });
        DisjointSetUnion dsu = new DisjointSetUnion(g.size()); List<Edge> chosen = new ArrayList<>(); long total = 0;
        for (Edge e : sorted) {
            m.edgeScans++;
            int a = dsu.find(e.from()), b = dsu.find(e.to()); m.dsuOps += 2;
            if (a != b) { dsu.union(a, b); m.dsuOps += 2; chosen.add(e); total += e.weight(); }
        }
        m.dsuOps = dsu.findCalls();
        return new MstResult(total, dsu.components(), List.copyOf(chosen), m);
    }
    public static MstResult prim(Graph g) {
        if (g.isDirected()) throw new IllegalArgumentException("MST requires an undirected graph");
        int n = g.size(); boolean[] used = new boolean[n]; Metrics m = new Metrics();
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(Edge::weight));
        List<Edge> chosen = new ArrayList<>(); long total = 0; int components = 0;
        for (int start = 0; start < n; start++) {
            if (used[start]) continue;
            components++; used[start] = true; pushNeighbors(g, start, used, pq, m);
            while (!pq.isEmpty()) {
                Edge e = pq.poll(); int v = e.to();
                if (used[v]) continue;
                used[v] = true; chosen.add(e); total += e.weight(); pushNeighbors(g, v, used, pq, m);
            }
        }
        return new MstResult(total, components, List.copyOf(chosen), m);
    }
    private static void pushNeighbors(Graph g, int u, boolean[] used, PriorityQueue<Edge> pq, Metrics m) {
        for (Edge e : g.neighbors(u)) { m.edgeScans++; if (!used[e.to()]) pq.add(e); }
    }
}
