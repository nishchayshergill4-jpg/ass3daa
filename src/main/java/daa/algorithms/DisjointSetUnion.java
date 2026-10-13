package daa.algorithms;

public final class DisjointSetUnion {
    private final int[] parent, size;
    private int components;
    private long findCalls, successfulUnions;
    public DisjointSetUnion(int n) {
        if (n < 0) throw new IllegalArgumentException("n cannot be negative");
        parent = new int[n]; size = new int[n]; components = n;
        for (int i = 0; i < n; i++) { parent[i] = i; size[i] = 1; }
    }
    public int find(int x) {
        findCalls++;
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }
    public boolean union(int a, int b) {
        int ra = find(a), rb = find(b);
        if (ra == rb) return false;
        if (size[ra] < size[rb]) { int t = ra; ra = rb; rb = t; }
        parent[rb] = ra; size[ra] += size[rb]; components--; successfulUnions++; return true;
    }
    public int components() { return components; }
    public long findCalls() { return findCalls; }
    public long successfulUnions() { return successfulUnions; }
}
