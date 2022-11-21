package datastruct;

public class UnionFind {
    // value for the root
    public static int ROOT = -1;
    // array that contains for each node its parent in the UnionFind tree
    private final int[] dad;
    // array with the rank of each node
    private final int[] rank;

    /**
     * Holds elements from 0 to size-1
     * @param size size of the union find data structure
     */
    public UnionFind(int size) {
        this.dad = new int[size];
        this.rank = new int[size];
    }

    /**
     * Make x the root of its own tree.
     *
     * @param x the element for which a tree is created
     */
    public void makeSet(int x) {
        dad[x] = ROOT;
        rank[x] = 0;
    }

    /**
     * Merges two sets into a single set
     * Union by rank
     *
     * @param r1 the root of the first tree
     * @param r2 the root of the second tree
     */
    public void union(int r1, int r2) {
        if(rank[r1] > rank[r2])
            dad[r2] = r1;
        else if(rank[r1] < rank[r2])
            dad[r1] = r2;
        else {
            dad[r1] = r2;
            rank[r2]++;
        }
    }

    /**
     * Returns the root of the tree containing x.
     * The function also performs path compression, meaning that all nodes on the path from x to the root
     * are set as children of the root
     *
     * @param x the node we want to find the root of
     * @return The root of the tree
     */
    public int find(int x) {
        // array containing visited nodes for path compression
        int[] s = new int[dad.length];
        // last valid index of array s
        int last = -1;
        // find the root of the tree for x by iterating over array dad
        int r = x;
        while(dad[r] != ROOT) {
            last++;
            s[last] = r;
            r = dad[r];
        }

        // path compression (set as children of root) with the visited nodes
        while(last >= 0) {
            dad[s[last]] = r;
            last--;
        }

        // return the root of the tree
        return r;
    }
}
