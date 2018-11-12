package dk.itu.algdes2018.groupi.graph;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

public class RGraph {
    private final SimpleDirectedWeightedGraph<RVertex, Object> graph;
    private final ArrayList<RVertex> reds;
    private RVertex source;
    private RVertex target;

    public RGraph() {
        graph = new SimpleDirectedWeightedGraph<>(Object.class);
        reds = new ArrayList<>();
        source = null;
        target = null;
    }
    
    private RGraph (SimpleDirectedWeightedGraph<RVertex, Object> graph, ArrayList<RVertex> reds, RVertex source, RVertex target) {
        this.graph = graph;
        this.reds = reds;
        this.source = source;
        this.target = target;
    }

    public List<RVertex> getReds() {
        return this.reds;
    }

	public Set<Object> getEdges() {
        return graph.edgeSet();
    }
    
	public void setSource(RVertex s) {
        this.source = s;
	}

	public void setTarget(RVertex t) {
        this.target = t;
    }
    
    public RVertex getEdgeFrom(Object edge) {
        return graph.getEdgeSource(edge);
    }

    public RVertex getEdgeTo(Object edge) {
        return graph.getEdgeTarget(edge);
    }

	public Set<Object> getIncomingEdges(RVertex v) {
		return graph.incomingEdgesOf(v);
	}

	public void setEdgeWeight(Object e, double w) {
        graph.setEdgeWeight(e, w);
	}

    public boolean addVertex(RVertex v) {
        boolean s = graph.addVertex(v);
        if (s && v.isRed()) {
            reds.add(v);
        }
        return s;
    }

	public boolean removeVertex(RVertex v) {
        boolean s = graph.removeVertex(v);
        if (s && v.isRed()) {
            reds.remove(v);
        }
        return s;
    }

    public boolean addEdge(RVertex from, RVertex to) {
        DefaultWeightedEdge e = new DefaultWeightedEdge();
        boolean s = graph.addEdge(from, to, e);
        graph.setEdgeWeight(e, 0);
        return s;
    }

	public boolean removeEdge(RVertex from, RVertex to) {
        Object o = graph.removeEdge(from, to);
        return o != null;
	}

    public RGraph copy() {
        SimpleDirectedWeightedGraph<RVertex, Object> g = (SimpleDirectedWeightedGraph<RVertex, Object>)graph.clone();
        ArrayList<RVertex> r = (ArrayList<RVertex>)reds.clone();
        return new RGraph(g, r, source, target);
    }

	public int BFSDepth() {
        BreadthFirstIterator<RVertex, Object> bfi = new BreadthFirstIterator<>(graph, source);
        
        while (bfi.hasNext()) {
            RVertex x = bfi.next();
            if (x == target) {
                break;
            }
        }
        
        try {
            return bfi.getDepth(target);
        }
        catch (NullPointerException ex) { //Thrown if there is no path to target
            return -1;
        }
	}

	public double DijkstraWeight() {
        GraphPath<RVertex, Object> gp = DijkstraShortestPath.findPathBetween(graph, source, target);
        return gp == null ? -1 : gp.getWeight();
	}
}