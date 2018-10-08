import java.util.ArrayList;
import java.util.Scanner;
import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        FlowGraph<Integer> G = readInput(args[0]);
        MinCut alg = new MinCut();
        Set<DiEdge> cut = alg.find(G);
        printResult(cut);
    }

    public static FlowGraph<Integer> readInput(String filepath) {
        // Does file exist?
        File f = new File(filepath);
        if(!f.exists()){
            System.out.printf("File '%s' does not exist. Skipping...\n", filepath);
            System.exit(-1);
        }

        try {
            
            Scanner sc = new Scanner(f);
            
            FlowGraph<Integer> graph = new FlowGraph<Integer>();

            ArrayList<Vertex> vertices = new ArrayList<Vertex>();
            int vertexCount = 0;
            int edgeCount = 0;
            boolean parsingVertices = true;

            if (sc.hasNext()) {
                vertexCount = sc.nextInt();
            }

            // System.out.println("Vertex count: " + vertexCount);

            int currentVertexIndex = 0;   // Used to assign index to vertices
            
            while (sc.hasNext()) {
                String line = sc.nextLine();
                
                if (line.equals("DESTINATIONS")) {
                    if (sc.hasNext()) {
                        edgeCount = Integer.parseInt(sc.nextLine());
                        // System.out.println("Edge count: " + edgeCount);
                    }
                    parsingVertices = false;
                    continue;
                } else {
                    if (parsingVertices) {
                        Vertex v = new Vertex(currentVertexIndex++, line);
                        vertices.add(v);
                    } else {
                        String[] components = line.split(" ");                          // v1 v2 capacity
                        
                        Vertex v1 = vertices.get(Integer.parseInt(components[0]));
                        Vertex v2 = vertices.get(Integer.parseInt(components[1]));
                        int capacity = Integer.parseInt(components[2]);

                        DiEdge v1_v2 = new DiEdge(v1, v2);                              // Create edge from v1 to v2
                        v1_v2.setValue(capacity);
                        v1.addEdge(v1_v2);

                        DiEdge v2_v1 = new DiEdge(v2, v1);                              // Create edge from v2 to v1
                        v2_v1.setValue(capacity);
                        v2.addEdge(v2_v1);
                    }
                }
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
        // print
    }
}