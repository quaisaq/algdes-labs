import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

import java.util.HashMap;

public class Parser{
    public static DiGraph parse(String filename){
        File f = new File(filename);

        try(Scanner sc = new Scanner(f)){
            int n = sc.nextInt();
            int m = sc.nextInt();
            int r = sc.nextInt();

            String beginVertexName = sc.next();
            String endVertexName = sc.next();

            sc.nextLine(); // This is to end the line after previous sc.next()

            // Parse all vertices (n)
            HashMap<String, Vertex> vertices = new HashMap<>();
            List<Vertex> reds = new ArrayList<>();
            for(int i = 0; i < n; i++){
                String line = sc.nextLine();
                boolean red = (line.endsWith("*")) ? true : false;
                String name = (line.split(" "))[0];
                Vertex v = new Vertex(name, red);
                
                vertices.put(name, v);
                if (red) {
                    reds.add(v);
                }
            }

            // Parse all edges
            for(int i = 0; i < m; i++){
                String v1 = sc.next();
                String dir = sc.next();
                String v2 = sc.next();
                
                if (dir == "->") {
                    DiEdge e = new DiEdge(vertices.get(v1), vertices.get(v2));
                    vertices.get(v1).addEdge(e);
                }
                else if (dir == "<-") { // not sure this exists
                    DiEdge e = new DiEdge(vertices.get(v2), vertices.get(v1));
                    vertices.get(v2).addEdge(e);
                }
                else if (dir == "--") {
                    DiEdge e = new DiEdge(vertices.get(v1), vertices.get(v2));
                    vertices.get(v1).addEdge(e);
                    DiEdge e2 = new DiEdge(vertices.get(v2), vertices.get(v1));
                    vertices.get(v2).addEdge(e2);
                }
                else {
                    System.err.printf("Direction of edge couldn't be parsed! Found this direction: %s%n", dir);
                }
            }

            if(Main.DEBUG){
                if(reds.size() != r){
                    System.err.println("We did not count to same amount of red vertices as specified in the input file!");
                    System.err.printf("Got %d from inputfile, but our count was %d%n", r, reds.size());
                    System.exit(3);
                }
            }

            return new DiGraph(vertices.get(beginVertexName), vertices.get(endVertexName), reds);
        }
        catch(Exception e){
            System.err.println("Something happened while parsing?");
            e.printStackTrace();
            System.exit(2);
        }

        // This will never happen, but java needs return...
        return null;
    }
}