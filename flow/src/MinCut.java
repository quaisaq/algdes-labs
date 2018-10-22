import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class MinCut {
    public Set<DiEdge> find(FlowGraph g) {
        PathFinder pathFinder = new PathFinder(g);
        while(true) {
            // Find a path
            System.out.println("Finding path");
            List<DiEdge> path = pathFinder.getPath();
            // If no path, get edges out of reachable set of vertices and return it.
            System.out.println("Found path of size: " + path.size());
            if(path.size() == 0) {
                return getCut(g.getSource());
            }
            // If there is a path, find bottleneck
            int min = Integer.MAX_VALUE;
            for(DiEdge e : path) {
                if(e.getValue() < min)
                    min = e.getValue();
            }
            System.out.println("Bottleneck of value: " + min);
            // Subtract bottleneck value from all edges in path
            for(DiEdge e : path) {
                ((RestDiEdge)e).setRestValue(e.getValue() - min);
            }
            // Start over
        }
    }

    private Set<DiEdge> getCut(Vertex source) {
        Set<Vertex> group = new HashSet<>();
        findGroup(source, group);
        Set<DiEdge> set = new HashSet<>();
        for(Vertex v : group) {
            for(DiEdge edge : v.getEdges()) {
                RestDiEdge e = (RestDiEdge)edge;
                if(!e.isReverse() && !group.contains(e.getTo()))
                    set.add(e);
            }
        }
        return set;
    }

    private void findGroup(Vertex source, Set<Vertex> group) {
        group.add(source);
        for(DiEdge edge : source.getEdges()) {
            RestDiEdge e = (RestDiEdge)edge;
            if(group.contains(e.getTo()) || e.isReverse())
                continue;
            if(e.getValue() != 0) {
                findGroup(e.getTo(), group);
            }
        }
    }
}