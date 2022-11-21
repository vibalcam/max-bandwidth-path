package algos;

import datastruct.LinkedList;
import datastruct.MaxHeap;
import datastruct.UnionFind;

public class DijkstraMaxHeap extends Dijkstra {
    public DijkstraMaxHeap(LinkedList[] g, int source) {
        super(g, source);
    }

    @Override
    protected void maxBW(LinkedList[] g) {
        int[] status = new int[g.length];
        // status, dad, and bwdith are initialized to 0 automatically
        MaxHeap maxHeap = new MaxHeap(g.length);

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

            // add to max heap
            maxHeap.add(current.data, current.weight);

            // check next edge
            current = current.next;
        }

        // main section of algorithm
        double minTmp;
        int maxFringer = maxHeap.pop();
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

                    // add to max heap
                    maxHeap.add(current.data, minTmp);
                } else if(status[current.data] == FRINGER && bWidth[current.data] < minTmp) {
                    bWidth[current.data] = minTmp;
                    setDad(current.data, maxFringer);

                    // update in max heap
                    maxHeap.deleteElement(current.data);
                    maxHeap.add(current.data, minTmp);
                }

                // check next edge
                current = current.next;
            }

            // pick fringer with largest bwidth
            if(maxHeap.isEmpty())
                maxFringer = -1;
            else
                maxFringer = maxHeap.pop();
        }
    }
}
