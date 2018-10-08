import java.awt.List;
import java.util.HashSet;
import java.util.Set;

public class MinCut {
    public Set<DiEdge> find(FlowGraph<Integer> g) {
        PathFinder pathFinder = new PathFinder(g);
        while(true) {
            // Find a path
            List<DiEdge<Integer>> path = pathFinder.getPath();
            // If no path, get edges out of reachable set of vertices and return it.
            if(path.size() == 0) {
                return getCut(g.getSource());
            }
            // If there is a path, find bottleneck
            int min = Integer.MAX_VALUE;
            for(DiEdge<Integer> e : path) {
                if(e.getValue() < min)
                    min = e.getValue();
            }
            // Subtract bottleneck value from all edges in path
            for(DiEdge<Integer> e : path) {
                e.setValue(min);
            }
            // Start over
        }
    }

    private Set<Vertex<Integer>> getCut(Vertex<Integer> source) {
        Set<Vertex<Integer>> set = new HashSet<>();
        for(RestDiEdge<Integer> e : source.getEdges()) {
            if(!e.isReverse() && e.getValue() != 0) {
                
            }
        }
    }
}