package daa.benchmark;

import daa.algorithms.*;
import daa.graph.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public final class Benchmark {
    private static final int[] SIZES={1000,3162,10000,31623,100000};
    private Benchmark() { }
    public static void main(String[] args) throws Exception {
        if(args.length>0){Graph g=GraphReader.read(Path.of(args[0]));System.out.println("n="+g.size()+", m="+g.edgeCount()+", directed="+g.isDirected());if(!g.isDirected()){MstResult k=MstAlgorithms.kruskal(g),p=MstAlgorithms.prim(g);System.out.println("Kruskal weight="+k.totalWeight()+", Prim weight="+p.totalWeight()+", components="+k.components());}else{SccResult s=Kosaraju.find(g);System.out.println("SCC count="+s.components().size());TopologicalResult t=Kahn.sort(g);System.out.println("Topological cycle="+t.hasCycle()+", order size="+t.order().size());if(!t.hasCycle())System.out.println("DAG distance sum="+DagShortestPath.compute(g).distanceSum());}return;}
        GraphGenerator.reset();
        Path out=Path.of("results","results.csv");Files.createDirectories(out.getParent());
        try(BufferedWriter w=Files.newBufferedWriter(out)){w.write("algorithm,graph,n,m,time_ms,dsu_ops,edge_scans,comparisons,max_depth,result\n");
            for(int n:SIZES)for(int factor:new int[]{4,20}){int m=n*factor;String label=factor==4?"sparse":"dense";
                Graph ug=GraphGenerator.undirected(n,m); MstResult kr=null,pr=null;
                long[] kt=new long[5],pt=new long[5]; for(int r=0;r<5;r++){long t=System.nanoTime();kr=MstAlgorithms.kruskal(ug);kt[r]=System.nanoTime()-t;t=System.nanoTime();pr=MstAlgorithms.prim(ug);pt[r]=System.nanoTime()-t;}
                if(kr.totalWeight()!=pr.totalWeight())throw new IllegalStateException("Kruskal/Prim disagreement n="+n+" m="+m);
                write(w,"Kruskal","undirected_"+label,n,m,median(kt),kr.metrics(),kr.totalWeight());write(w,"Prim","undirected_"+label,n,m,median(pt),pr.metrics(),pr.totalWeight());
                Graph dg=GraphGenerator.directed(n,m);long[] st=new long[5];SccResult sr=null;for(int r=0;r<5;r++){long t=System.nanoTime();sr=Kosaraju.find(dg);st[r]=System.nanoTime()-t;}write(w,"Kosaraju","directed_"+label,n,m,median(st),sr.metrics(),sr.components().size());
                Graph dag=GraphGenerator.dag(n,m);TopologicalResult tr=null;DagShortestPath.Result dr=null;long[] at=new long[5],dt=new long[5];for(int r=0;r<5;r++){long t=System.nanoTime();tr=Kahn.sort(dag);at[r]=System.nanoTime()-t;t=System.nanoTime();dr=DagShortestPath.compute(dag);dt[r]=System.nanoTime()-t;}if(tr.hasCycle()||tr.order().size()!=n)throw new IllegalStateException("Invalid topological order");write(w,"Kahn","dag_"+label,n,m,median(at),tr.metrics(),tr.order().size());write(w,"DagShortestPath","dag_"+label,n,m,median(dt),dr.metrics(),dr.distanceSum());
                System.out.printf(Locale.ROOT,"n=%d m=%d MST=%d SCC=%d DAGdist=%d%n",n,m,kr.totalWeight(),sr.components().size(),dr.distanceSum());
            }
        }
        System.out.println("Wrote "+out.toAbsolutePath());
    }
    private static long median(long[] a){Arrays.sort(a);return a[a.length/2]/1_000_000;}
    private static void write(BufferedWriter w,String alg,String graph,int n,int m,long ms,Metrics x,long result)throws IOException{w.write(String.format(Locale.ROOT,"%s,%s,%d,%d,%d,%d,%d,%d,%d,%d%n",alg,graph,n,m,ms,x.dsuOps,x.edgeScans,x.comparisons,x.maxDepth,result));}
}
