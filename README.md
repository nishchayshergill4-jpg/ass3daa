# DAA Assignment 3 — Graphs

Java 17+ Maven project implementing DSU, Kruskal, Prim, iterative Kosaraju, Kahn topological ordering, and shortest paths in a DAG.

## Requirements
- JDK 17 or newer
- Maven 3.9+
- Python 3 with `matplotlib` only if regenerating charts

## Build and test
```bash
mvn clean test
```

## Run demo on the small sample graph
```bash
mvn -q exec:java -Dexec.args="data/sample_connected.txt"
```

## Run the full benchmark
```bash
mvn -q exec:java
```
It writes `results/results.csv`. Full run covers five vertex sizes and sparse/dense densities. The largest case contains two million edges and may take a few minutes depending on your machine.

## Regenerate plots
```bash
python generate_plots.py
```

## Project layout
- `src/main/java/daa/graph`: graph representation and text input
- `src/main/java/daa/algorithms`: algorithms and metrics
- `src/main/java/daa/benchmark`: deterministic graph generator and benchmark runner
- `src/test/java/daa`: JUnit 5 tests
- `data/`: three sample graphs
- `results/`: CSV output and charts

## Notes
- Graph vertices are numbered from `0` to `n-1`.
- An undirected edge is stored once in the logical edge list and twice in adjacency lists (except self-loops, which are handled as one adjacency insertion in this implementation).
- Kosaraju uses an explicit stack, not recursive DFS, to remain safe for 100,000 vertices.
- Counters are collected while algorithms execute. Wall-clock time is reported as the median of five runs.
- The benchmark generator uses a fixed seed (`42`) for reproducibility. If any reference values differ from the assignment handout, inspect the precise random-number consumption order and compare generator details before changing algorithm code.

## AI-use acknowledgement
AI assistance was used to scaffold and review parts of this project. The student should verify the implementation, understand the code, run the tests and benchmark, and be able to explain all submitted work.
