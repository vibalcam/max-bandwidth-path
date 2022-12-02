package datastruct;

import java.util.Arrays;
import java.util.Random;

public class TestGraphs {
    private static final int NUM_VERTICES = 5000;

    // degree parameters
    private static final double G1_AVERAGE_DEGREE = 6;
    private static final double G2_PERCENTAGE_NEIGHBORS = 0.2;

    // Weight parameters
    private static final double MIN_WEIGHT = 1;
    private static final double MAX_WEIGHT = 200;

    /*
        0-200 weights
        Instead of 1000, maybe 900
     */

    /**
     * Shuffles an array using the seed and returns k elements
     * Implements Fisher–Yates shuffle (Durstenfeld algorithm)
     * <a href="https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle">Wikipedia</a>
     *
     * Basic explanation:
     * The algorithm randomly chooses an unsampled element and move it to the end of the list by swapping
     * it with the last unsampled element at each iteration
     *
     * @param x the array to shuffle
     * @param seed The seed for the random number generator.
     */
    private static int[] shuffleArray(int[] x, int k, long seed)
    {
        // can also be done in place instead of using a return array, but this way we return only k elements
        int[] ret = new int[k];
        Random rnd = new Random(seed);
        for (int i = x.length - 1; i > x.length - k; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            ret[x.length-i-1] = x[index];
            x[index] = x[i];
            x[i] = ret[x.length-i-1];
        }
        return ret;
    }

    private static int[] shuffleArray(int[] x, long seed) {
        return shuffleArray(x, x.length, seed);
    }

    private static double getRandomWeight(Random rdm) {
        return MIN_WEIGHT + (MAX_WEIGHT - MIN_WEIGHT) * rdm.nextDouble();
    }

    /**
     * Generates a cycle with a given number of vertices
     *
     * Note:
     * Since we are creating a cycle from scratch, there is no need to shuffle the array of LinkedLists.
     * That would provide the same cycle but with different node numbering
     *
     * @param nVertices the number of vertices in the graph
     * @return An adjacency list (LinkedList array of size nVertices).
     */
    private static LinkedList[] getRandomCycle(int nVertices, long seed) {
        LinkedList[] adjList = new LinkedList[nVertices];
        Random rdm = new Random(seed);

        // add edges from (n-1,0),(0,1)...(n-2,n-1)
        int n1;
        n1 = nVertices-1;
        adjList[n1] = new LinkedList();
        double w;
        for(int k=0; k<nVertices; k++) {
            // get random weight
            w = getRandomWeight(rdm);
            // add edge (k-1,k) to n1 (linked list already created)
            adjList[n1].add(k, w);
            // add edge (k,k-1) to k (first create list if not already created)
            if(k != nVertices - 1)
                adjList[k] = new LinkedList();
            adjList[k].add(n1, w);
            // update n1
            n1 = k;
        }
        return adjList;
    }

// NO NEED TO MAKE CYCLE RANDOM (JUST REORDER NODES AND IT IS NO LONGER RANDOM)
//    private static LinkedList[] getRandomCycle(int nVertices, long seed) {
//        // get array with vertices and create empty adjacency list
//        LinkedList[] adjList = new LinkedList[nVertices];
//        int [] nodes = new int[nVertices];
//        for (int k=0;k < nodes.length;k++) {
//            adjList[k] = new LinkedList();
//            nodes[k] = k;
//        }
//
//        // randomly order the array
//        nodes = shuffleArray(nodes, seed);
//
//        // form a cycle with the random order
//        int n1,n2;
//        n1 = nodes[nodes.length-1];
//        // add edges from (n-1,0),(0,1)...(n-2,n-1)
//        for(int k=0; k<nodes.length; k++) {
//            // add edge (k-1,k) to adjacency list
//            n2 = nodes[k];
//            adjList[n1].add(n2);
//            adjList[n2].add(n1);
//            // update n1
//            n1 = n2;
//        }
//        return adjList;
//    }

    // imp use configuration method to give different degree to each node
    /**
     * Creating a graph with a given number of vertices and average vertex degree.
     *
     * @param nVertices number of vertices
     * @param seed random seed
     * @return adjacency list for the graph
     */
    public static LinkedList[] getGraph1(int nVertices, long seed) {
        // get random cycle
        LinkedList[] adjList = getRandomCycle(nVertices, seed);

        /*
         Randomly choose (AVERAGE_DEGREE * nVertices / 2 - nVertices) edges (some edges already included in cycle)

         Total possible edges tE = nVertices * (nVertices - 3) since each node already has 2 edges in the cycle
         Choose randomly the required amount of edges. Let x be one of the selected...
         x // (nVertices - 3) -> source node can be [0, nVertices-1] (each node is equally probable 1/nVertices)
         x %  (nVertices - 3) -> target node can be [0, (nVertices-4)] (each node is equally probable 1/(nVertices-3))
         Target node will be (target + source + 2) % nVertices
            Min: (0 + source + 2) % nVertices = (source + 2) % nVertices (first element at a distance of 2)
            Max: (nVertices - 4 + source + 2) % nVertices = (source - 2) % nVertices (second element at a distance of 2)
         */

        // calculate number of edges to create given an average vertex degree and the n edges in cycle
        int nEdges = (int) (G1_AVERAGE_DEGREE * nVertices / 2 - nVertices);

        // create array with possible edges to be chosen
        int [] edges = new int[nVertices * (nVertices-3)];
        for (int k=0;k < edges.length;k++)
            edges[k]=k;

        // choose nEdges randomly
        edges = shuffleArray(edges, nEdges, seed);

        // add edges to adjacency list
        int n1,n2;
        double w;
        Random rdm = new Random(seed);
        for (int e : edges) {
            // get random weight
            w = getRandomWeight(rdm);
            // get edge
            n1 = e / (nVertices-3);
            n2 = e % (nVertices-3);

            // add edge
            adjList[n1].add(n2, w);
            adjList[n2].add(n1, w);
        }
        return adjList;
    }

    // imp make faster
    public static LinkedList[] getGraph2(int nVertices, long seed) {
        // get random cycle
        LinkedList[] adjList = getRandomCycle(nVertices, seed);

        // build adjacency matrix
        int[][] adjMatrix = new int[adjList.length][adjList.length];
        LinkedList.Node current;
        for(int i=0; i < adjList.length; i++) {
            current = adjList[i].head;
            while(current != null) {
                adjMatrix[i][current.data] = 1;
                current = current.next;
            }
        }

        // create array with possible neighbors to be chosen
        int [] possibleNeighbors = new int[nVertices];
        for (int k=0;k < possibleNeighbors.length;k++)
            possibleNeighbors[k]=k;

        // calculate number of neighbors for each node
        int nNeighbors = (int) ((nVertices-1) * G2_PERCENTAGE_NEIGHBORS);

        int[] newNeighbors;
        LinkedList l1, l2;
        double w;
        Random rdm = new Random(seed);
        int j, size, tmp;
        // for each node
        for (int i=0; i< adjList.length; i++) {
//            System.out.println(i);
            l1 = adjList[i];

            // each vertex is adjacent to G2_PERCENTAGE_NEIGHBORS * (n-1)
            size = l1.size();
            if(size >= nNeighbors)
                continue;
            newNeighbors = shuffleArray(possibleNeighbors, nNeighbors - size, seed);

            // for each of the chosen indices
            for(int k=0; k < newNeighbors.length; k++) {
                j = newNeighbors[k];
                l2 = adjList[j];
                size = l2.size();

                // get random weight
                w = getRandomWeight(rdm);

//                // if element already present, ignore
//                if(l1.getIndex(j) != -1)
//                    continue;

                // while element selected is already present
                tmp = j;
                while(adjMatrix[i][j] == 1 || size >= nNeighbors || j==i) {
                    j++;
                    if(j == adjList.length)
                        j=0;
                    l2 = adjList[j];
                    size = l2.size();

                    // if already did a cycle, break
                    // once cycled it means all vertices already have the required amount of neighbors
                    if(tmp == j)
                        return adjList;
                }

                // when element is not adjacent, add edge
                l1.add(j, w);
                adjMatrix[i][j] = 1;
                l2.add(i, w);
                adjMatrix[j][i] = 1;
            }
        }
        return adjList;
    }

//    public static LinkedList[] getGraph2(int nVertices, long seed) {
//        // get random cycle
//        LinkedList[] adjList = getRandomCycle(nVertices, seed);
//
//        // create array with possible neighbors to be chosen
//        int [] possibleNeighbors = new int[nVertices-1];
//        for (int k=0;k < possibleNeighbors.length;k++)
//            possibleNeighbors[k]=k;
//
//        int nNeighbors = (int) ((nVertices-1) * G2_PERCENTAGE_NEIGHBORS);
//        int[] newNeighbors;
//        LinkedList l1, l2;
//        double w;
//        Random rdm = new Random(seed);
//        int j, size;
//
//        // for each node
//        for (int i=0; i< adjList.length; i++) {
//            l1 = adjList[i];
//
//            // each vertex is adjacent to G2_PERCENTAGE_NEIGHBORS * (n-1)
//            size = l1.size();
//            if(size >= nNeighbors)
//                continue;
//            newNeighbors = shuffleArray(possibleNeighbors, nNeighbors - size, seed);
//
//            // for each of the chosen indices
//            for(int k=0; k < newNeighbors.length; k++) {
//                j = newNeighbors[k];
//                l2 = adjList[j];
//                size = l2.size();
//
//                // get random weight
//                w = getRandomWeight(rdm);
//
//                // if element already present, ignore
//                if(l1.getIndex(j) != -1)
//                    continue;
//
////                // while element selected is already present
////                while(l1.getIndex(j) != -1 || size >= nNeighbors) {
////                    j++;
////                    if(j == adjList.length)
////                        j=0;
////                    l2 = adjList[j];
////                    size = l2.size();
////                }
//
//                // when element is not already adjacent
//                l1.add(j, w);
//                l2.add(i, w);
//            }
//        }
//        return  adjList;
//    }

    // TEST METHOD
    public static double getAverageDegree(LinkedList[] g1) {
        double nDeg = 0;
        for (LinkedList l:g1)
            nDeg += l.size();
        return nDeg / g1.length;
    }

    public static double getPercentageNeighbors(LinkedList[] g2) {
        double sumPercNeigh = 0;
        for (LinkedList l:g2)
            sumPercNeigh += l.size();
        return sumPercNeigh / (g2.length - 1) / g2.length;
    }

    public static void main(String[] arg)
    {
        int seed = 123;
        int nVertices = 5000;

        // test get cycle
        LinkedList[] cycle = getRandomCycle(4, seed);
        System.out.println(Arrays.toString(cycle));

        // test graph 1
        LinkedList[] g1 = getGraph1(nVertices, seed);
        System.out.println(g1.length);
        System.out.println(getAverageDegree(g1));

        // test graph 2
        LinkedList[] g2 = getGraph2(nVertices, seed);
        System.out.println(g2.length);
        System.out.println(getPercentageNeighbors(g2));
    }
}
