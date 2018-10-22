import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class MinCut {
    public Set<DiEdge> find(FlowGraph g) {
        PathFinder pathFinder = new PathFinder(g);
        while(true) {
            // Find a path
            List<DiEdge> path = pathFinder.getPath();
            // If no path, get edges out of reachable set of vertices and return it.
            if(path.size() == 0) {
                return getCut(g.getSource());
            }
            // If there is a path, find bottleneck
            int min = Integer.MAX_VALUE;
            for(DiEdge e : path) {
                if(e.getValue() < min)
                    min = e.getValue();
            }
            // Subtract bottleneck value from all edges in path
            for(DiEdge e : path) {
                ((RestDiEdge)e).setRestValue(min);
            }
            // Start over
        }
    }

    private Set<DiEdge> getCut(Vertex source) {
        Set<DiEdge> set = new HashSet<>();
        for(DiEdge edge : source.getEdges()) {
            RestDiEdge e = (RestDiEdge)edge;
            if(!e.isReverse()) {
                if(e.getValue() == 0) {
                    set.add(e);
                } else {
                    set.addAll(getCut(e.getTo()));
                }
            }
        }
        return set;
    }
}