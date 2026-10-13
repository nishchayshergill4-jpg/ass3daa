package daa.algorithms;
import java.util.List;
public record TopologicalResult(List<Integer> order, boolean hasCycle, Metrics metrics) { }
