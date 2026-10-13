package daa.algorithms;

import daa.graph.Edge;
import java.util.List;

public record MstResult(long totalWeight, int components, List<Edge> selectedEdges, Metrics metrics) { }
