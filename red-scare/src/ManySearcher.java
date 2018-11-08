import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Searches the graph for a solution to the "many" problem
 */
public class ManySearcher {
    public static int search(DiGraph graph) {
        /**
         * This function uses a stack to traverse the graph in reverse.
         * The stack is quickly filled with many vertices.
         * The vertices will automatically be added in the order that they should be visited in order for the next vertice to be counted efficiently
         */
        HashMap<Vertex, Integer> redCounter = new HashMap<>();
        HashSet<Vertex> inStack = new HashSet<>();
        Stack<Vertex> s = new Stack<>();

        Vertex source = graph.getSource();
        Vertex target = graph.getTarget();

        redCounter.put(source, source.isRed() ? 1 : 0);
        s.push(target);

        while (!s.empty()) {
            Vertex v = s.pop();
            inStack.remove(v);
            //if (redCounter.containsKey(v)) continue; //skip - we have already been here //TODO: Check if this check is necessary
            
            List<Vertex> missingBackNeighbors = v.getInEdges().stream().map(e -> e.getFrom()).filter(v2 -> !redCounter.containsKey(v2)).collect(Collectors.toList());
            if (missingBackNeighbors.size() != 0) {
                //add `v` back on stack, after all missing vertices going into it have been done
                s.add(v);
                inStack.add(v);
                boolean added = false;
                for (Vertex v2 : missingBackNeighbors) {
                    if (!inStack.contains(v2)) {
                        s.add(v2);
                        inStack.add(v2);
                        added = true;
                    }
                }
                //If we added something, we should run that before we run this again
                if (added) {
                    continue;
                }
                else {
                    //otherwise, we remove ourselves from the stack
                    s.pop();
                    inStack.remove(v);
                }
            }
            
            //We are now in a vertex, and we have been in all its incoming neighbors.
            int redCount = 0;
            for (DiEdge e : v.getInEdges()) {
                Vertex f = e.getFrom();
                redCount = Math.max(redCount, inStack.contains(f) ? 0 : redCounter.get(f));
            }

            //update counter with this vertex
            redCounter.put(v, redCount + (v.isRed() ? 1 : 0));
        }

        return redCounter.get(target);
    }
}