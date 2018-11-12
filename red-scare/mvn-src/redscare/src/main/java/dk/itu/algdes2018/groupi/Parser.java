package dk.itu.algdes2018.groupi;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import dk.itu.algdes2018.groupi.graph.RGraph;
import dk.itu.algdes2018.groupi.graph.RVertex;

import java.util.HashMap;

public class Parser{
    public static RGraph parse(String filename) {
        if (Main.DEBUG) System.err.println("Current directory: " + new File(".").getAbsolutePath());
        File f = new File(filename);

        //Create scanner for file - automatically closed by try-with-resource statement
        try(Scanner sc = new Scanner(f)){
            RGraph graph = new RGraph();

            int n = sc.nextInt();
            int m = sc.nextInt();
            int r = sc.nextInt();

            String s = sc.next();
            String t = sc.next();

            sc.nextLine(); // This is to end the line after previous sc.next()

            // Parse all vertices (n)
            HashMap<String, RVertex> vertices = new HashMap<>();
            for(int i = 0; i < n; i++){
                String line = sc.nextLine();
                boolean red = (line.endsWith("*")) ? true : false;
                String name = (line.split(" "))[0];
                RVertex v = new RVertex(name, red);
                
                vertices.put(name, v);
                if (!graph.addVertex(v)) {
                    System.err.println("Failed to add vertex to graph! (" + name + (red ? " *" : "") + ")");
                }
            }

            // Parse all edges
            for(int i = 0; i < m; i++){
                String v1 = sc.next();
                String dir = sc.next();
                String v2 = sc.next();
                
                if (dir.equals("->")) { //directed edge from v1 to v2
                    RVertex from = vertices.get(v1);
                    RVertex to = vertices.get(v2);
                    boolean s1 = graph.addEdge(from, to);
                    if (!s1) {
                        System.err.println("Failed to add edge to graph! (" + from.getName() + " -> " + to.getName() + ")");
                    }
                }
                else if (dir.equals("<-")) { //directed edge from v2 to v1 - not sure this exists
                    RVertex to = vertices.get(v1);
                    RVertex from = vertices.get(v2);
                    boolean s2 = graph.addEdge(from, to);
                    if (!s2) {
                        System.err.println("Failed to add edge to graph! (" + to.getName() + " <- " + from.getName() + ")");
                    }
                }
                else if (dir.equals("--")) { //undirected edge
                    //we handle undirected edges as two directed edges, one in each direction
                    RVertex from = vertices.get(v1);
                    RVertex to = vertices.get(v2);
                    boolean s3 = graph.addEdge(from, to);
                    boolean s4 = graph.addEdge(to, from);
                    if (!s3) {
                        System.err.println("Failed to add edge to graph (s3)! (" + from.getName() + " -- " + to.getName() + ")");
                    }
                    if (!s4) {
                        System.err.println("Failed to add edge to graph (s4)! (" + from.getName() + " -- " + to.getName() + ")");
                    }
                }
                else {
                    System.err.printf("Direction of edge couldn't be parsed! Found this direction: %s%n", dir);
                }
            }

            if(Main.DEBUG){
                List<RVertex> reds = graph.getReds();
                if(reds.size() != r){
                    System.err.println("We did not count to same amount of red vertices as specified in the input file!");
                    System.err.printf("Got %d from inputfile, but our count was %d%n", r, reds.size());
                    System.exit(3);
                }
            }
            
            graph.setSource(vertices.get(s));
            graph.setTarget(vertices.get(t));
            
            return graph;
        }
        catch(Exception e){
            System.err.println("Something happened while parsing?");
            e.printStackTrace();
            System.exit(2);

            // This will never happen, but java needs return...
            return null;
        }
    }
}