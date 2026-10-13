package daa;
import daa.algorithms.DisjointSetUnion;import org.junit.jupiter.api.Test;import static org.junit.jupiter.api.Assertions.*;
class DSUTest {@Test void unionsReduceComponentCount(){DisjointSetUnion d=new DisjointSetUnion(5);assertEquals(5,d.components());assertTrue(d.union(0,1));assertTrue(d.union(1,2));assertFalse(d.union(0,2));assertEquals(3,d.components());assertEquals(d.find(0),d.find(2));}}
