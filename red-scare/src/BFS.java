import java.util.*;
import java.util.function.BiFunction;

public class BFS {
    /**
     * A short method for the bigger search function.
     * Will use the graph's predefined source and target for the search.
     * @param g             The graph to search
     * @param shouldVisit   A function that determines whether a vertex should be visited, based on the vertex the search is currently at.
     * @return              A list of edges describing the shortest path from the source to the target vertex.
     */
    public static List<DiEdge> search(DiGraph g, BiFunction<Vertex, Vertex, Boolean> shouldVisit) {
        return search(g, g.getSource(), g.getTarget(), shouldVisit);
    }

    /**
     * Finds the shortest path in the graph from the given source to the given target.
     * This search uses BFS and therefore does not take edge-weights into account. 
     * @param g             The graph to search
     * @param source        The vertex to start the path at
     * @param target        The vertex to end the path at
     * @param shouldVisit   A function that determines whether a vertex should be visited, based on the vertex the search is currently at.
     * @return              A list of edges describing the shortest path from the source to the target vertex.
     */
    public static List<DiEdge> search(DiGraph g, Vertex source, Vertex target, BiFunction<Vertex, Vertex, Boolean> shouldVisit){
        HashMap<Vertex, DiEdge> previous = new HashMap<>();
        HashSet<Vertex> visited = new HashSet<>();
        LinkedList<DiEdge> q = new LinkedList<>();

        //Add the source vertex to the set of visited nodes
        visited.add(source);
        
        //Start search by adding the edges out of the source vertex to the search queue
        for(DiEdge e : source.getOutEdges()){
            //Only add the edge to the queue, if we are allowed to do it.
            //SPECIAL: If the edge leads to the target immediately, we can also use it (special case given from assignment)
            if (shouldVisit.apply(source, e.getTo()) || e.getTo() == target) {
                q.add(e);
            }
        }

        //Iterate over search queue in the order the edges are inserted - BFS
        while(!q.isEmpty()){
            DiEdge e = q.pop();

            Vertex from = e.getFrom();
            Vertex to = e.getTo();

            // If we haven't visited the `to` vertex
            if(!visited.contains(to)){

                // If our shouldVisit function approves of `from` and `to`
                if(shouldVisit.apply(from, to)){

                    if(Main.DEBUG) System.err.printf("Going from %s to %s%n", from.getName(), to.getName());
                    
                    // Record how we got to this vertex - used for finding the path later
                    previous.put(to, e);

                    // If we found the target, end search here
                    if(to.equals(target)){
                        break;
                    }

                    // Otherwise, iterate over edges going out of `to` and add them to the search queue
                    for(DiEdge e2 : to.getOutEdges()){
                        q.add(e2);
                    }

                    //Mark `to` as visited
                    visited.add(to);
                }
            }
        }

        if(!previous.containsKey(target)){
            // If the target was never visited, there was no path to the target.
            return null;            
        }

        // Build result list
        List<DiEdge> result = new ArrayList<>();
        DiEdge current = previous.get(target);
        while(!source.equals(current.getFrom())){
            result.add(current);
            current = previous.get(current.getFrom());
        }
        result.add(current);    // remember to add last edge (from `source`)

        return result;
    }
}