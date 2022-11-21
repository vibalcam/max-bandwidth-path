package algos;

import datastruct.LinkedList;

public class DFS {
    private static final int VISITED = 1;
    private static final int NOT_VISITED = 0;

    private static final int ROOT = -1;


    public static LinkedList dfs_main(LinkedList[] g, int source, int target) {
        int[] dad = new int[g.length];
        int[] visited = new int[g.length];
        double[] bw = new double[g.length];

        // set values for source
        dad[source] = ROOT;
        visited[source] = VISITED;
        bw[source] = Double.POSITIVE_INFINITY;

        // run dfs
        dfs_rec(g, source, visited, dad, bw);

        // check if target has been visited, i.e. is reachable
        if(visited[target] == NOT_VISITED)
            return null;

        // get path to target
        LinkedList p = new LinkedList();
        double maxBw = bw[target];
        p.add(target, maxBw);

        int x = dad[target];
        while(x != ROOT) {
            p.add(x, maxBw);
            x = dad[x];
        }

        return p;
    }

    private static void dfs_rec(LinkedList[] g, int x, int[] visited, int[] dad, double[] bw) {
        visited[x] = VISITED;
        LinkedList.Node current = g[x].head;
        // for each edge of x
        int w;
        while(current != null) {
            w = current.data;
            if(visited[w] == NOT_VISITED){
                dad[w] = x;
                bw[w] = bw[x] < current.weight ? bw[x] : current.weight;
                dfs_rec(g, w, visited, dad, bw);
            }
            // go to next edge
            current = current.next;
        }

    }
}
