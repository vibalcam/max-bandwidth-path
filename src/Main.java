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
        // one for each of the three algorithms + one for DFS
        double [][] tG1 = new double[4][nRuns*nPairs];
        double [][] tG2 = new double[4][nRuns*nPairs];
        double [][] t = new double[4][nRuns*nPairs];
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
                        System.out.println("Graph2 run " + i + " average percentage neighbors " + getPercentageNeighbors(g));
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
                    toc = System.currentTimeMillis();
                    t[2][r] = toc - tic;
                    tic = System.currentTimeMillis();
                    kruskalBw = kruskal.getBandWidth(source, target);
                    toc = System.currentTimeMillis();
                    t[2][r] += toc - tic;
                    t[3][r] = toc - tic;
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
        StringBuffer s = new StringBuffer();
        double[] meanTG1 = new double[tG1.length];
        for(int i=0; i< tG1.length; i++) {
            s.append("Algorithm ")
                    .append(i)
                    .append("\t")
                    .append(Arrays.toString(tG1[i]))
                    .append("\n");
            for(double k : tG1[i])
                meanTG1[i] += k;
        }
        for(int i=0; i< tG1.length; i++) {
            meanTG1[i] /= tG1[0].length;
        }
        System.out.println("Total running time G1 (Dijkstra, DijkstraMaxHeap, Kruskal, DFS): " + Arrays.toString(meanTG1));
        System.out.println("Full info running time G1 (Dijkstra, DijkstraMaxHeap, Kruskal, DFS): \n" + s);

        // calculate mean times for g2
        s = new StringBuffer();
        double[] meanTG2 = new double[tG2.length];
        for(int i=0; i< tG2.length; i++) {
            s.append("Algorithm ")
                    .append(i)
                    .append("\t")
                    .append(Arrays.toString(tG2[i]))
                    .append("\n");
            for(double k : tG2[i])
                meanTG2[i] += k;
        }
        for(int i=0; i< tG2.length; i++) {
            meanTG2[i] /= tG2[0].length;
        }
        System.out.println("Total running time G2 (Dijkstra, DijkstraMaxHeap, Kruskal, DFS): " + Arrays.toString(meanTG2));
        System.out.println("Full info running time G2 (Dijkstra, DijkstraMaxHeap, Kruskal, DFS): \n" + s);
    }

//    Graph1 run 0 average degree 6.0
//    Graph1 run 1 average degree 6.0
//    Graph1 run 2 average degree 6.0
//    Graph1 run 3 average degree 6.0
//    Graph1 run 4 average degree 6.0
//    Graph2 run 0 average percentage neighbors 0.18222068413682738
//    Graph2 run 1 average percentage neighbors 0.18162208441688338
//    Graph2 run 2 average percentage neighbors 0.18234326865373074
//    Graph2 run 3 average percentage neighbors 0.18203848769753952
//    Graph2 run 4 average percentage neighbors 0.1815999199839968
//    Total running time G1 (Dijkstra, DijkstraMaxHeap, Kruskal, DFS): [146.36, 8.72, 223.76, 0.92]
//    Full info running time G1 (Dijkstra, DijkstraMaxHeap, Kruskal, DFS):
//    Algorithm 0	[212.0, 157.0, 99.0, 234.0, 145.0, 114.0, 111.0, 173.0, 141.0, 153.0, 117.0, 208.0, 193.0, 140.0, 121.0, 95.0, 74.0, 123.0, 106.0, 208.0, 218.0, 161.0, 126.0, 141.0, 89.0]
//    Algorithm 1	[55.0, 12.0, 6.0, 8.0, 7.0, 5.0, 5.0, 5.0, 3.0, 5.0, 5.0, 15.0, 4.0, 5.0, 4.0, 6.0, 4.0, 3.0, 6.0, 5.0, 16.0, 18.0, 5.0, 6.0, 5.0]
//    Algorithm 2	[683.0, 134.0, 855.0, 170.0, 148.0, 130.0, 242.0, 210.0, 607.0, 195.0, 172.0, 288.0, 135.0, 100.0, 108.0, 151.0, 133.0, 133.0, 115.0, 152.0, 260.0, 132.0, 104.0, 128.0, 109.0]
//    Algorithm 3	[5.0, 1.0, 0.0, 0.0, 1.0, 0.0, 1.0, 1.0, 0.0, 0.0, 1.0, 3.0, 1.0, 1.0, 1.0, 0.0, 1.0, 1.0, 2.0, 0.0, 0.0, 1.0, 1.0, 1.0, 0.0]
//
//    Total running time G2 (Dijkstra, DijkstraMaxHeap, Kruskal, DFS): [443.12, 188.64, 15326.04, 1.2]
//    Full info running time G2 (Dijkstra, DijkstraMaxHeap, Kruskal, DFS):
//    Algorithm 0	[310.0, 231.0, 1666.0, 69.0, 118.0, 389.0, 423.0, 2904.0, 534.0, 133.0, 356.0, 299.0, 307.0, 337.0, 397.0, 449.0, 226.0, 189.0, 182.0, 154.0, 789.0, 163.0, 170.0, 171.0, 112.0]
//    Algorithm 1	[278.0, 56.0, 87.0, 45.0, 58.0, 341.0, 58.0, 78.0, 70.0, 54.0, 344.0, 313.0, 278.0, 289.0, 292.0, 389.0, 151.0, 145.0, 137.0, 138.0, 777.0, 92.0, 82.0, 89.0, 75.0]
//    Algorithm 2	[15261.0, 14932.0, 8945.0, 12691.0, 20258.0, 21535.0, 18213.0, 22090.0, 17760.0, 15749.0, 13707.0, 14352.0, 15394.0, 14884.0, 13335.0, 15245.0, 11941.0, 10509.0, 10450.0, 13434.0, 24567.0, 14140.0, 16848.0, 15164.0, 11747.0]
//    Algorithm 3	[0.0, 1.0, 0.0, 0.0, 3.0, 2.0, 5.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 3.0, 1.0, 0.0, 1.0, 6.0, 2.0, 1.0, 1.0, 0.0, 1.0, 1.0]
}