import java.util.ArrayList;
import java.util.Scanner;
import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        FlowGraph G = readInput(args[0]);
        MinCut alg = new MinCut();
        Set<DiEdge> cut = alg.find(G);
        printResult(cut);
    }

    public static FlowGraph readInput(String filepath) {
        // Does file exist?
        File f = new File(filepath);
        if(!f.exists()){
            System.out.printf("File '%s' does not exist. Skipping...\n", filepath);
            System.exit(-1);
        }

        try {
            
            Scanner sc = new Scanner(f);
            
            FlowGraph graph = new FlowGraph();

            ArrayList<Vertex> vertices = new ArrayList<>();

            assert sc.hasNext();
            int vertexCount = Integer.parseInt(sc.nextLine());
            for(int currentVertexIndex = 0; currentVertexIndex < vertexCount; currentVertexIndex++) {
                assert sc.hasNext();
                String line = sc.nextLine();
                Vertex v = new Vertex(currentVertexIndex, line);
                vertices.add(v);
            }
            assert sc.hasNext();
            int edgeCount = Integer.parseInt(sc.nextLine());
            for(int currentEdgeIndex = 0; currentEdgeIndex < edgeCount; currentEdgeIndex++) {
                assert sc.hasNext();
                String line = sc.nextLine();
                String[] components = line.split(" ");                          // v1 v2 capacity
                        
                Vertex v1 = vertices.get(Integer.parseInt(components[0]));
                Vertex v2 = vertices.get(Integer.parseInt(components[1]));
                int capacity = Integer.parseInt(components[2]);
                if(capacity == -1)
                    capacity = Integer.MAX_VALUE;

                DiEdge v1_v2 = new RestDiEdge(v1, v2);                              // Create edge from v1 to v2
                v1_v2.setValue(capacity);
                v1.addEdge(v1_v2);

                DiEdge v2_v1 = new RestDiEdge(v2, v1);                              // Create edge from v2 to v1
                v2_v1.setValue(capacity);
                v2.addEdge(v2_v1);
            }
            
            assert vertexCount == vertices.size();

            graph.setSource(vertices.get(0));
            graph.setSink(vertices.get(vertices.size() - 1));
            return graph;

        } catch (FileNotFoundException e) {
            System.out.printf("Could not open file '%s'\nError:\n%s", filepath, e);
            System.exit(-1);
        }

        System.exit(-1);
        return null;
    }
    
    public static void printResult(Set<DiEdge> cut) {
        List<DiEdge> cutList = new ArrayList<>(cut);
        Comparator<DiEdge> sortByFrom = (e1, e2) -> Integer.compare(e1.getFrom().getId(), e2.getFrom().getId());
        Comparator<DiEdge> sortByTo = (e1, e2) -> Integer.compare(e1.getTo().getId(), e2.getTo().getId());
        cutList.sort(sortByFrom.thenComparing(sortByTo));
        // print
        for(DiEdge e : cutList) {
            RestDiEdge edge = (RestDiEdge)e;
            System.out.println(e.getFrom().getId() + " " + e.getTo().getId() + " " + edge.getCapactiy());
        }
    }
}