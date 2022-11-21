import algos.Dijkstra;
import algos.Kruskal;
import algos.DijkstraMaxHeap;
import datastruct.LinkedList;

import java.util.Arrays;
import java.util.Random;

import static datastruct.TestGraphs.*;

public class Main {
    public static void main(String[] args) {
        int seed = 123;
        int nVertices = 5000;
        int nRuns = 5;
        int nPairs = 5;
        Random rdm = new Random(seed);

        LinkedList[] g = new LinkedList[nVertices];
        double [][] tG1 = new double[3][nRuns*nPairs];
        double [][] tG2 = new double[3][nRuns*nPairs];
        double [][] t = new double[3][nRuns*nPairs];;
        double tic,toc;
        int r;

        int source,target;
        Dijkstra dijkstra;
        double dijkstraBw;
        DijkstraMaxHeap dijkstraMaxHeap;
        double dijkstraMaxHeapBw;
        Kruskal kruskal;
        double kruskalBw;

        // run for both types of graphs
        for(int j = 0; j<2; j++) {
            // run multiple times
            for (int i = 0; i < nRuns; i++) {
                // advance seed to get different graphs
                seed++;
                // build test graphs
                switch (j) {
                    case 0:
                        g = getGraph1(nVertices, seed);
                        t = tG1;
                        System.out.println("Graph1 run " + i + " average degree " + getAverageDegree(g));
                        break;
                    case 1:
                        g = getGraph2(nVertices, seed);
                        t = tG2;
                        System.out.println("Graph1 run " + (i+nRuns) + " average percentage neighbors " + getPercentageNeighbors(g));
                        break;
                }

                // for each graph pick 5 randomly selected source-target pairs
                for (int k = 0; k < nPairs; k++) {
                    r = i * nPairs + k;

                    source = rdm.nextInt(nVertices);
                    target = rdm.nextInt(nVertices);
                    while (source == target)
                        target = rdm.nextInt(nVertices);

                    // run algorithms and record running time
                    tic = System.currentTimeMillis();
                    dijkstra = new Dijkstra(g, source);
                    // get path takes longer than just get bw, so we will test that so it can be compared to kruskal
                    dijkstraBw = dijkstra.getPath(target).getNode(0).weight;
                    toc = System.currentTimeMillis();
                    t[0][r] = toc - tic;
                    dijkstra = null;

                    tic = System.currentTimeMillis();
                    dijkstraMaxHeap = new DijkstraMaxHeap(g, source);
                    // get path takes longer than just get bw, so we will test that so it can be compared to kruskal
                    dijkstraMaxHeapBw = dijkstraMaxHeap.getPath(target).getNode(0).weight;
                    toc = System.currentTimeMillis();
                    t[1][r] = toc - tic;
                    dijkstraMaxHeap = null;

                    tic = System.currentTimeMillis();
                    kruskal = new Kruskal(g);
                    kruskalBw = kruskal.getBandWidth(source, target);
                    toc = System.currentTimeMillis();
                    t[2][r] = toc - tic;
                    kruskal = null;

                    // print bw results
//                    System.out.println(dijkstraBw + " " + dijkstraMaxHeapBw + " " + kruskalBw);

                    // check correctness
                    if (dijkstraBw != dijkstraMaxHeapBw || dijkstraMaxHeapBw != kruskalBw)
                        throw new RuntimeException("Incorrect result");
                }
            }
        }

        // calculate mean times for g1
        double[] meanTG1 = new double[tG1.length];
        for(int i=0; i< tG1.length; i++) {
            for(double k : tG1[i])
                meanTG1[i] += k;
        }
        for(int i=0; i< tG1.length; i++) {
            meanTG1[i] /= tG1[0].length;
        }
        System.out.println("Total running time G1 (Dijkstra, DijkstraMaxHeap, Kruskal): " + Arrays.toString(meanTG1));

        // calculate mean times for g2
        double[] meanTG2 = new double[tG2.length];
        for(int i=0; i< tG2.length; i++) {
            for(double k : tG2[i])
                meanTG2[i] += k;
        }
        for(int i=0; i< tG2.length; i++) {
            meanTG2[i] /= tG2[0].length;
        }
        System.out.println("Total running time G2 (Dijkstra, DijkstraMaxHeap, Kruskal): " + Arrays.toString(meanTG2));
    }

//    Total running time G1 (Dijkstra, DijkstraMaxHeap, Kruskal): [138.24, 6.8, 234.56]
//    Total running time G2 (Dijkstra, DijkstraMaxHeap, Kruskal): [238.44, 175.0, 17274.64]
}