import java.util.*;
import java.util.function.BiFunction;

public class BFS{
    public static List<DiEdge> search(DiGraph g, BiFunction<Vertex, Vertex, Boolean> shouldVisit){
        HashMap<Vertex, DiEdge> previous = new HashMap<>();
        HashSet<Vertex> visited = new HashSet<>();
        LinkedList<DiEdge> q = new LinkedList<>();

        Vertex s = g.getSource();
        visited.add(s);
        for(DiEdge e : s.getEdges()){
            q.add(e);
        }

        while(!q.isEmpty()){
            DiEdge e = q.pop();

            Vertex from = e.getFrom();
            Vertex to = e.getTo();

            // If we havent visited to vertex
            if(!visited.contains(to)){

                // If our shouldVisit function approves of from and to
                if(shouldVisit.apply(from, to)){

                    // Path stuff
                    previous.put(to, e);

                    // If we find target, break
                    if(to.equals(g.getTarget())){
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

        if(!previous.containsKey(g.getTarget())){
            // Could not find path
            return null;            
        }

        // Build result list
        List<DiEdge> result = new ArrayList<>();
        DiEdge current = previous.get(g.getTarget());
        while(!g.getSource().equals(current.getFrom())){
            result.add(current);
            current = previous.get(current.getFrom());
        }

        return result;
    }
}