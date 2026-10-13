package daa.algorithms;

import daa.graph.Edge;
import daa.graph.Graph;
import java.util.*;

public final class Kahn {
    private Kahn() { }
    public static TopologicalResult sort(Graph g) {
        if (!g.isDirected()) throw new IllegalArgumentException("Topological sort expects directed graph");
        int n = g.size(); int[] indegree = new int[n];
        for (Edge e : g.edges()) indegree[e.to()]++;
        ArrayDeque<Integer> q = new ArrayDeque<>(); for (int i=0;i<n;i++) if (indegree[i]==0) q.add(i);
        List<Integer> order = new ArrayList<>(n); Metrics m = new Metrics();
        while (!q.isEmpty()) { int u=q.remove(); order.add(u); for (Edge e:g.neighbors(u)) { m.edgeScans++; indegree[e.to()]--; if(indegree[e.to()]==0) q.add(e.to()); } }
        boolean cycle = order.size()!=n; return new TopologicalResult(List.copyOf(order), cycle, m);
    }
}
