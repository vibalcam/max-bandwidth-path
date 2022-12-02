package algos;

import datastruct.LinkedList;

public class Dijkstra {
    // Status array
    protected static final int UNSEEN = 0;
    protected static final int FRINGER = 1;
    protected static final int INTREE = 2;

    // Dad array
    protected static final int UNREACHABLE = -1;
    protected static final int ROOT = -2;

    // array with the parent nodes in the max bandwidth path (parent node + 1)
    // parent node 0 means the node cannot be reached (value -1)
    // parent node -1 means the node is the root/source (value -2)
    private final int[] dad;
    // array with the bandwidth values
    protected final double[] bWidth;
    // source node
    protected final int source;

    public Dijkstra(LinkedList[] g, int source) {
        this.dad = new int[g.length];
        this.bWidth = new double[g.length];
        this.source = source;
        // run max bandwidth
        maxBW(g);
    }

    public LinkedList getPath(int target) {
        if(getDad(target) == UNREACHABLE)
            return null;

        // get bw for the edges in the path
        double bw = getBandWidth(target);

        // get the path
        LinkedList p = new LinkedList();
        p.add(target, bw);

        int dad = getDad(target);
        while(dad != ROOT) {
            p.add(dad, bw);
            dad = getDad(dad);
        }

        return p;
    }

    public double getBandWidth(int target) {
        return bWidth[target];
    }

    protected int getDad(int idx) {
        return dad[idx] - 1;
    }

    protected void setDad(int idx, int value) {
        dad[idx] = value + 1;
    }

    protected void maxBW(LinkedList[] g) {
        int[] status = new int[g.length];
        // status, dad, and bwdith are initialized to 0 automatically

        // set variables for source node
        status[source] = INTREE;
        bWidth[source] = Double.POSITIVE_INFINITY;
        setDad(source, ROOT);

        // add adjacent nodes to source
        LinkedList.Node current = g[source].head;
        while(current != null) {
            status[current.data] = FRINGER;
            bWidth[current.data] = current.weight;
            setDad(current.data, source);

            // check next edge
            current = current.next;
        }

        // main section of algorithm
        double minTmp;
        int maxFringer = getMaxFringer(g, status);
        while(maxFringer != -1) {
            // set largest fringer as intree
            status[maxFringer] = INTREE;

            // for all adjacent nodes to largest fringer
            current = g[maxFringer].head;
            while(current != null) {
                minTmp = bWidth[maxFringer] < current.weight ? bWidth[maxFringer] : current.weight;
                if(status[current.data] == UNSEEN) {
                    status[current.data] = FRINGER;
                    bWidth[current.data] = minTmp;
                    setDad(current.data, maxFringer);
                } else if(status[current.data] == FRINGER && bWidth[current.data] < minTmp) {
                    bWidth[current.data] = minTmp;
                    setDad(current.data, maxFringer);
                }

                // check next edge
                current = current.next;
            }

            // pick fringer with largest bwidth
            maxFringer = getMaxFringer(g, status);
        }
    }

    // imp use a double linked list so it can be deleted in constant time, or keep in memory last value
    private int getMaxFringer(LinkedList[] g, int[] status) {
        int max = -1;
        double maxVal = 0;
        double tmp;

        for(int k=0; k < status.length; k++) {
            tmp = bWidth[k];
            if(status[k] == FRINGER && (max == -1 || maxVal < tmp)) {
                max = k;
                maxVal = tmp;
            }
        }
        return max;
    }
}
