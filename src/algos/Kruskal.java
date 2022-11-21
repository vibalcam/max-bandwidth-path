package algos;

import datastruct.LinkedList;
import datastruct.MaxHeap;
import datastruct.UnionFind;

public class Kruskal {
    // imp use another sorting algorithm instead of heapsort (merge sort O(n log n)) linear time sorting

    // todo finish

    // maximum spanning tree
    private final LinkedList[] maxSpanningTree;

    public Kruskal(LinkedList[] g) {
        this.maxSpanningTree = new LinkedList[g.length];
        // run max spanning tree
        maxSpanningTree(g);
    }

    public LinkedList getPath(int source, int target) {
        return DFS.dfs_main(maxSpanningTree, source, target);
    }

    public double getBandWidth(int source, int target) {
        return getPath(source, target).head.weight;
    }

    private void maxSpanningTree(LinkedList[] g) {
        int n = g.length;
        UnionFind unionFind = new UnionFind(n);
        // get list of edge
        double[] edges_weights = new double[n * (n-1)];
        Edge[] edges_info = new Edge[n * (n-1)];

        int m = 0;
        LinkedList.Node current;
        for(int k=0; k< n; k++){
            current = g[k].head;
            while(current != null) {
                // make sure no duplicates by only adding when i < j
                if(k >= current.data) {
                    current = current.next;
                    continue;
                }
                // add edge to list
                edges_weights[m] = current.weight;
                edges_info[m] = new Edge(k, current.data);
                m++;

                // go to next node
                current = current.next;
            }
            // make set for node
            unionFind.makeSet(k);
            maxSpanningTree[k] = new LinkedList();
        }

        // sort edges
        int[] sorted = MaxHeap.heapSort(edges_weights, m, false);

        // main section of algorithm
        Edge e;
        int r1,r2;
        for(int k : sorted) {
            // get edge
            e = edges_info[k];
            // check if edge is in the same piece
            r1 = unionFind.find(e.i);
            r2 = unionFind.find(e.j);
            // if in different pieces
            if(r1 != r2) {
                // add edge to tree
                maxSpanningTree[e.i].add(e.j, edges_weights[k]);
                maxSpanningTree[e.j].add(e.i, edges_weights[k]);
                // union of the pieces
                unionFind.union(r1,r2);
            }
        }
    }

    private static class Edge {
        int i;
        int j;

        public Edge(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }
}
