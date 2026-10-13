package daa.algorithms;

import daa.graph.Edge;
import daa.graph.Graph;
import java.util.*;

public final class DagShortestPath {
    private DagShortestPath() { }
    public record Result(long[] distances, int[] parent, long distanceSum, Metrics metrics) {
        public List<Integer> pathTo(int target) {
            if (target < 0 || target >= parent.length) throw new IndexOutOfBoundsException();
            if (distances[target] == Long.MAX_VALUE) return List.of();
            ArrayList<Integer> path = new ArrayList<>();
            for (int v=target; v!=-1; v=parent[v]) path.add(v);
            Collections.reverse(path); return List.copyOf(path);
        }
    }
    public static Result compute(Graph g) {
        if (!g.isDirected()) throw new IllegalArgumentException("DAG shortest paths expects directed graph");
        TopologicalResult top = Kahn.sort(g); if (top.hasCycle()) throw new IllegalArgumentException("Graph contains a directed cycle");
        int n=g.size(); int[] indegree=new int[n]; for(Edge e:g.edges()) indegree[e.to()]++;
        long[] dist=new long[n]; int[] parent=new int[n]; Arrays.fill(dist,Long.MAX_VALUE); Arrays.fill(parent,-1);
        for(int i=0;i<n;i++) if(indegree[i]==0) dist[i]=0;
        Metrics m=new Metrics();
        for(int u:top.order()) {
            if(dist[u]==Long.MAX_VALUE) continue;
            for(Edge e:g.neighbors(u)) { m.edgeScans++; long candidate=dist[u]+e.weight(); m.comparisons++; if(candidate<dist[e.to()]) { dist[e.to()]=candidate; parent[e.to()]=u; } }
        }
        long sum=0; for(long d:dist) if(d!=Long.MAX_VALUE) sum+=d;
        return new Result(dist,parent,sum,m);
    }
}
