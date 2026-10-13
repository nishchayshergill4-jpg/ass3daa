from pathlib import Path
import csv, math
import matplotlib.pyplot as plt

csv_path = Path("results/results.csv")
plot_dir = Path("results/plots")
plot_dir.mkdir(parents=True, exist_ok=True)
if not csv_path.exists():
    raise SystemExit("Run `mvn -q exec:java` first to generate results/results.csv")
rows=[]
with csv_path.open(newline="", encoding="utf-8") as f:
    rows=list(csv.DictReader(f))
# Plot 1: measured edge scans as an algorithm-work proxy.
plt.figure()
for alg in sorted({r['algorithm'] for r in rows}):
    for density in ("sparse", "dense"):
        selected=[r for r in rows if r['algorithm']==alg and density in r['graph']]
        selected.sort(key=lambda r:int(r['n']))
        if selected:
            plt.loglog([int(r['n']) for r in selected], [max(1,int(r['edge_scans'])+int(r['dsu_ops'])+int(r['comparisons'])) for r in selected], marker='o', label=f"{alg} {density}")
plt.xlabel("Vertices n"); plt.ylabel("Counted operations (sum of counters)"); plt.title("Operations vs n"); plt.legend(fontsize=7); plt.tight_layout(); plt.savefig(plot_dir/"operations_vs_n.png", dpi=160); plt.close()
# Plot 2: edge scans per logical edge.
plt.figure()
for alg in sorted({r['algorithm'] for r in rows}):
    selected=[r for r in rows if r['algorithm']==alg]
    selected.sort(key=lambda r:int(r['n']))
    if selected:
        plt.plot([int(r['n']) for r in selected], [int(r['edge_scans'])/max(1,int(r['m'])) for r in selected], marker='o', label=alg)
plt.xscale('log'); plt.xlabel("Vertices n"); plt.ylabel("edgeScans / m"); plt.title("Scans per edge"); plt.legend(fontsize=8); plt.tight_layout(); plt.savefig(plot_dir/"scans_per_edge.png", dpi=160); plt.close()
# Plot 3: fraction of edges examined by Kruskal (this implementation scans all sorted edges).
plt.figure()
for density in ("sparse", "dense"):
    selected=[r for r in rows if r['algorithm']=="Kruskal" and density in r['graph']]
    selected.sort(key=lambda r:int(r['n']))
    if selected:
        plt.plot([int(r['n']) for r in selected], [int(r['edge_scans'])/max(1,int(r['m'])) for r in selected], marker='o', label=density)
plt.xscale('log'); plt.xlabel("Vertices n"); plt.ylabel("edgeScans / m"); plt.title("Share of edges examined by Kruskal"); plt.legend(); plt.tight_layout(); plt.savefig(plot_dir/"kruskal_edge_share.png", dpi=160); plt.close()
print(f"Plots written to {plot_dir}")
