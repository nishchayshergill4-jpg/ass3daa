package daa.algorithms;

import daa.graph.Edge;
import daa.graph.Graph;
import java.util.*;

/** Iterative Kosaraju; explicit frames preserve recursive DFS finish order. */
public final class Kosaraju {
    private Kosaraju() { }
    private record Frame(int vertex, int nextIndex) { }
    public static SccResult find(Graph g) {
        if (!g.isDirected()) throw new IllegalArgumentException("SCC expects directed graph");
        int n = g.size(); boolean[] seen = new boolean[n]; List<Integer> order = new ArrayList<>(n); Metrics m = new Metrics();
        for (int s = 0; s < n; s++) if (!seen[s]) {
            ArrayDeque<Frame> stack = new ArrayDeque<>(); seen[s] = true; stack.push(new Frame(s, 0));
            while (!stack.isEmpty()) {
                m.maxDepth = Math.max(m.maxDepth, stack.size()); Frame f = stack.pop(); List<Edge> adj = g.neighbors(f.vertex());
                if (f.nextIndex() < adj.size()) {
                    stack.push(new Frame(f.vertex(), f.nextIndex()+1)); Edge e = adj.get(f.nextIndex()); m.edgeScans++;
                    if (!seen[e.to()]) { seen[e.to()] = true; stack.push(new Frame(e.to(), 0)); }
                } else order.add(f.vertex());
            }
        }
        Graph rev = g.reversed(); Arrays.fill(seen, false); List<List<Integer>> comps = new ArrayList<>();
        for (int i = order.size()-1; i >= 0; i--) {
            int s = order.get(i); if (seen[s]) continue;
            List<Integer> comp = new ArrayList<>(); ArrayDeque<Integer> stack = new ArrayDeque<>(); stack.push(s); seen[s] = true;
            while (!stack.isEmpty()) {
                m.maxDepth = Math.max(m.maxDepth, stack.size()); int u = stack.pop(); comp.add(u);
                for (Edge e : rev.neighbors(u)) { m.edgeScans++; if (!seen[e.to()]) { seen[e.to()] = true; stack.push(e.to()); } }
            }
            comps.add(List.copyOf(comp));
        }
        return new SccResult(List.copyOf(comps), m);
    }
}
