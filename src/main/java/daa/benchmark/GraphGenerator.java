package daa.benchmark;

import daa.graph.Graph;
import java.util.*;

public final class GraphGenerator {
    private static Random undirectedRandom = new Random(42);
    private static Random directedRandom = new Random(42);
    private static Random dagRandom = new Random(42);
    private GraphGenerator() { }
    public static void reset() { undirectedRandom = new Random(42); directedRandom = new Random(42); dagRandom = new Random(42); }
    private static void shuffle(int[] a, Random rnd) { for(int i=a.length-1;i>0;i--){int j=rnd.nextInt(i+1);int t=a[i];a[i]=a[j];a[j]=t;} }
    public static Graph undirected(int n, int m) {
        if(m<n-1) throw new IllegalArgumentException("Connected undirected graph needs m >= n-1");
        Random rnd = undirectedRandom; Graph g=new Graph(n,false); int[] perm=new int[n]; for(int i=0;i<n;i++)perm[i]=i; shuffle(perm,rnd);
        for(int i=1;i<n;i++){int u=perm[rnd.nextInt(i)],v=perm[i];g.addEdge(u,v,1+rnd.nextInt(1000));}
        while(g.edgeCount()<m){int a=rnd.nextInt(n),b=rnd.nextInt(n);if(b==a)continue;g.addEdge(a,b,1+rnd.nextInt(1000));}
        return g;
    }
    public static Graph directed(int n,int m) {
        Random rnd = directedRandom; Graph g=new Graph(n,true);
        while(g.edgeCount()<m){int a=rnd.nextInt(n),b=rnd.nextInt(n);if(a==b)continue;g.addEdge(a,b,1+rnd.nextInt(1000));}
        return g;
    }
    public static Graph dag(int n,int m) {
        Random rnd = dagRandom; Graph g=new Graph(n,true); int[] rank=new int[n];for(int i=0;i<n;i++)rank[i]=i;shuffle(rank,rnd);
        while(g.edgeCount()<m){int a=rnd.nextInt(n),b=rnd.nextInt(n);if(a==b)continue;if(rank[a]>rank[b]){int t=a;a=b;b=t;}g.addEdge(a,b,1+rnd.nextInt(1000));}
        return g;
    }
}
