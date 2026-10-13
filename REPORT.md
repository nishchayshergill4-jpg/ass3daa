# Assignment 3 Report — Graph Algorithms

## 1. Complexity table

| Algorithm | Time | Extra space | Reason |
|---|---:|---:|---|
| DSU `find` / `union` | Amortized `O(α(V))` per operation | `O(V)` for parent/size arrays | Union by size keeps trees shallow and path compression flattens paths. |
| Kruskal | `O(E log E + E α(V))` | `O(V + E)` | Sorting dominates; DSU checks whether each edge joins different components. |
| Prim with binary heap | `O((V+E) log V)` (usual bound) | `O(V+E)` | Each candidate edge is handled through a priority queue and adjacency lists. |
| Kosaraju | `O(V+E)` | `O(V+E)` | Two DFS passes and a reversed graph visit every vertex and edge a constant number of times. |
| Kahn topological sort | `O(V+E)` | `O(V)` | Computes indegrees, queues zero-indegree vertices and scans outgoing edges once. |
| DAG shortest paths | `O(V+E)` | `O(V)` beyond graph storage | Topological ordering plus one relaxation pass; negative edge weights are safe in a DAG. |

## 2. Correctness: MST cut property

The cut property says that for any cut that separates a graph's vertices into two sets, a minimum-weight edge crossing that cut is safe to include in some minimum spanning tree. Kruskal processes edges from lightest to heaviest and accepts an edge only when its endpoints are in different DSU components, so it never creates a cycle. At each accepted step, the edge is a light edge connecting two current components and is safe by the cut property. Prim maintains one growing tree and chooses the lightest edge from the current tree to a vertex outside it. That edge is the lightest edge crossing the cut between the tree and the remaining vertices, so it is safe too. Repeating safe choices produces an MST when the graph is connected. If the graph is disconnected, both implementations produce a minimum spanning forest, one tree per connected component.

## 3. Correctness: DAG relaxation

A topological order places every source of an edge before its destination. Initially, every zero-indegree vertex receives distance zero, representing a virtual super-source connected to all DAG sources. Assume that when a vertex is processed, its shortest distance has already been determined from all possible predecessors. Every predecessor appears earlier in topological order, so all incoming relaxations have already been considered. Relaxing each outgoing edge therefore propagates the best distance to its destination. By induction over the topological order, each reachable vertex has its shortest distance after its turn is processed. A DAG has no directed cycles, so no path can return to an earlier vertex and require another pass. Negative weights are safe because the ordering still prevents a later vertex from improving an already processed predecessor; in a general graph, negative cycles can make shortest paths undefined.

## 4. Which algorithm when?

| Situation | Choice | Why |
|---|---|---|
| Edge list, need an MST | Kruskal | Sorting edges and DSU make edge-list processing natural. |
| Adjacency list, need an MST | Prim | Grows a tree through neighboring edges and a priority queue. |
| Sparse graph | Often Kruskal or heap-based Prim | Both avoid scanning a dense matrix; benchmark actual constants. |
| Dense graph | Prim can be a practical choice | It avoids sorting every edge in some implementations; this version uses a heap, so compare measured counters/time. |
| Need strongly connected components | Kosaraju | Two linear graph traversals; iterative DFS avoids JVM recursion limits. |
| Need ordering and cycle detection in a dependency graph | Kahn | If fewer than `V` vertices are output, a directed cycle exists. |
| DAG with possibly negative edge weights | DAG shortest paths | Linear time after topological ordering; no negative cycle can exist in a DAG. |
| General graph with negative edges | Bellman-Ford (not implemented here) | DAG relaxation's one-pass guarantee does not hold in arbitrary cyclic graphs. |

## 5. Counters and benchmark interpretation

The benchmark records `edgeScans`, `dsuOps`, `comparisons`, and `maxDepth` inside algorithm execution. For Kosaraju, the expected scan count is `2E` because both passes traverse all directed adjacency edges. Kahn and DAG relaxation each scan `E` edges on a DAG. Kruskal sorts the logical edge list and examines edges until its loop ends; this implementation counts all sorted edges, including edges that would close a cycle. Prim's scan count depends on the edges incident to vertices as they enter the forest. Ratios such as `edgeScans/(V+E)` for traversals should remain bounded as graph size grows. Kruskal includes a sorting term, so its work grows slightly faster than linear in `E`.

## 6. Timing note

The benchmark takes five measurements and reports their median in milliseconds. Runtime depends on CPU, JVM warm-up, memory, and background activity, so a single timing is not reliable evidence of asymptotic complexity. Operation counters are the reproducible evidence; use timing as supplementary information. Run `mvn -q exec:java` to create the machine-specific `results/results.csv`, then update the plots and conclusions from your own results.

## 7. Implementation/testing notes

The graph class supports directed and undirected graphs and stores adjacency lists. DSU uses union by size and path compression. Kosaraju uses an explicit stack and is tested on a 100,000-vertex path. Tests cover MST agreement and disconnected forests, cycle detection, SCCs, and negative-weight DAG paths. The deterministic benchmark uses seed 42. Before submission, run all tests, inspect generated reference values against the handout, and update this report with the measured ratios and fitted log-log slopes from your run.
