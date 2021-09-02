//Marcello Sautto
//Project 2

import java.util.*;
import java.io.*;

public class Project2 {
    Scanner sc = null;
    Graph graph = null;
    public Project2(){

        try {
            sc = new Scanner(new File("input.txt"));
        }
        catch(FileNotFoundException e) {
            System.out.println("Error: Input File Not Found");
            System.exit(1);
        }

        int nV = sc.nextInt();
        sc.nextLine();
        graph = new Graph(nV); //creates instance of graph
    }

    class Graph
    {
        class Edge
        {
            private int startVertex;
            private int endVertex;
            private int weight;
            Edge(int s, int e, int w){
                startVertex = s;
                endVertex = e;
                weight = w;
            }
        }

        int numVertices;
        ArrayList<Edge> edgeArrayList = new ArrayList<Edge>();

        Graph(int V) {
            numVertices = V;

            for(int i = 1; i < numVertices; i++) {
                sc.nextLine();
                for(int j = 0; j < i; j++) {
                    edgeArrayList.add(new Edge(i, j, sc.nextInt()));
                }
            }

        }
    }

    class sortByWeight implements Comparator<Graph.Edge> {
        //sort by weight smallest to largest
        public int compare(Graph.Edge a, Graph.Edge b)
        {
            return a.weight - b.weight;
        }
    }

    class Node{
        private int parent;
        private int size;
        Node(int p, int s)
        {
            parent = p;
            size = s;
        }

    };

    class UnionFind {
        private void Union(ArrayList<Node> subsetArrayList, int v1, int v2) {

            //find root of each subset
            int tempV1Root = Find(subsetArrayList, v1);
            int tempV2Root = Find(subsetArrayList, v2);

            //assign the largest subset with the larger root of the two
            if (subsetArrayList.get(tempV1Root).size < subsetArrayList.get(tempV2Root).size)
                subsetArrayList.get(tempV1Root).parent = tempV2Root;
            else if (subsetArrayList.get(tempV1Root).size > subsetArrayList.get(tempV2Root).size)
                subsetArrayList.get(tempV2Root).parent = tempV1Root;
            else {
                subsetArrayList.get(tempV2Root).parent = tempV1Root;
                subsetArrayList.get(tempV1Root).size++;
            }
        }

        //Find root of a given vertex while setting the parents of each vertex traversed
        private int Find(ArrayList<Node>subsetArrayList, int vertex){
            while(subsetArrayList.get(vertex).parent != vertex){
                vertex = subsetArrayList.get(vertex).parent;
            }
            return subsetArrayList.get(vertex).parent;
        }

    }

    public static void main(String[] args){

        Project2 p2 = new Project2(); //read text file and set edge array list
        p2.kruskalsAlg(); //run Kruskal's Algorithm
    }

    private void kruskalsAlg(){
        //sort array list
        graph.edgeArrayList.sort(new sortByWeight());

        //create array list for minimum spanning tree and the subsets found during the runtime of Kruskal's
        ArrayList<Graph.Edge>mST = new ArrayList<Graph.Edge>();
        ArrayList<Node>subsetArrayList = new ArrayList<Node>();

        // Create V subsets with single elements
        for (int i = 0; i < graph.numVertices; i++)
            subsetArrayList.add(new Node(i, 0));

        UnionFind uf = new UnionFind();
        //go through all edges
        for(int edgeIndex = 0; mST.size() < graph.numVertices-1; edgeIndex++) {
            //find the parent of the start and end node of the current edge
            int tempV1 = uf.Find(subsetArrayList,graph.edgeArrayList.get(edgeIndex).startVertex);
            int tempV2 = uf.Find(subsetArrayList,graph.edgeArrayList.get(edgeIndex).endVertex);

            //if this edge does not form a cycle, then add it to the minimum spanning tree and unionize the subsets
            if(tempV1 != tempV2){
                mST.add(graph.edgeArrayList.get(edgeIndex));
                uf.Union(subsetArrayList, tempV1, tempV2);
            }
        }

        //print Minimum Spanning Tree
        printMST(mST);
    }

    private void printMST(ArrayList<Graph.Edge>mST){
        int mstWeight = 0;
        for (Graph.Edge edge : mST) {
            edge.startVertex++;
            edge.endVertex++;
            System.out.println(edge.endVertex + " " + edge.startVertex + " " + edge.weight);
            mstWeight += edge.weight;
        }
        System.out.println(mstWeight);
    }
}
