import java.util.*;
import java.util.function.BiFunction;

public class BFS {
    public static List<DiEdge> search(DiGraph g, BiFunction<Vertex, Vertex, Boolean> shouldVisit) {
        return search(g, g.getSource(), g.getTarget(), shouldVisit);
    }

    public static List<DiEdge> search(DiGraph g, Vertex source, Vertex target, BiFunction<Vertex, Vertex, Boolean> shouldVisit){
        HashMap<Vertex, DiEdge> previous = new HashMap<>();
        HashSet<Vertex> visited = new HashSet<>();
        LinkedList<DiEdge> q = new LinkedList<>();

        visited.add(source);
        for(DiEdge e : source.getEdges()){
            if(shouldVisit.apply(source, e.getTo())) {
                q.add(e);
            }
        }

        while(!q.isEmpty()){
            DiEdge e = q.pop();

            Vertex from = e.getFrom();
            Vertex to = e.getTo();

            // If we havent visited to vertex
            if(!visited.contains(to)){

                // If our shouldVisit function approves of from and to
                if(shouldVisit.apply(from, to)){
                    if(Main.DEBUG) System.err.printf("Going from %s to %s%n", from.getName(), to.getName());
                    // Path stuff
                    previous.put(to, e);

                    // If we find target, break
                    if(to.equals(target)){
                        break;
                    }

                    // Iterate over edges from to-vertex and add edges to queue
                    for(DiEdge e2 : to.getEdges()){
                        q.add(e2);
                    }
                    visited.add(to);
                }
            }
        }

        if(!previous.containsKey(target)){
            // Could not find path
            return null;            
        }

        // Build result list
        List<DiEdge> result = new ArrayList<>();
        DiEdge current = previous.get(target);
        while(!source.equals(current.getFrom())){
            result.add(current);
            current = previous.get(current.getFrom());
        }
        result.add(current);    // remember to add last edge (from source)

        return result;
    }
}