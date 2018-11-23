package dk.itu.algdes2018.groupi.graph;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.flow.DinicMFImpl;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.alg.shortestpath.BellmanFordShortestPath;
// import org.jgrapht.alg.shortestpath.NegativeCycleDetectedException;


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
    
    public RVertex getSource(){
        return this.source;
    }

    public RVertex getTarget(){
        return this.target;
    }

	public void setTarget(RVertex t) {
        this.target = t;
    }
    
	public double getEdgeWeight(Object e) {
		return graph.getEdgeWeight(e);
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

    public Set<Object> getOutgoingEdges(RVertex v) {
		return graph.outgoingEdgesOf(v);
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
    
    public boolean isUndirected() {
        class DiEdge {
            public final RVertex from;
            public final RVertex to;

            public DiEdge(RVertex from, RVertex to) {
                this.from = from;
                this.to = to;
            }

            public boolean equals(Object o) {
                if (o == this) {
                    return true;
                }
                if (o instanceof DiEdge) {
                    DiEdge e = (DiEdge)o;
                    return e.from.equals(this.from) && e.to.equals(this.to);
                }
                return false;
            }

            public int hashCode() {
                return from.hashCode() + to.hashCode();
            }

            public DiEdge inverse() {
                return new DiEdge(to, from);
            }
        }

        //Making new set because I don't want to take each vertex out every time we do a test.
        HashSet<DiEdge> set = new HashSet<>();
        for (Object e : getEdges()) {
            RVertex from = getEdgeFrom(e);
            RVertex to = getEdgeTo(e);
            DiEdge de = new DiEdge(from, to);
            set.add(de);
        }
        
        for (DiEdge e : set) {
            if (!set.contains(e.inverse())) {
                return false;
            }
        }
        
        return true;
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
    
	public double maxFlow() {
        DinicMFImpl<RVertex, Object> mf = new DinicMFImpl<>(graph);
        return mf.calculateMaximumFlow(getSource(), getTarget());
    }

	public double BellmanFordWeight() {
        try{
            GraphPath<RVertex, Object> gp = BellmanFordShortestPath.findPathBetween(graph, source, target);
            return gp == null ? -1 : gp.getWeight() * -1;
        }
        catch (Exception e){
            System.err.println("(This is red text) Negative cycle in graph. There is no solution to the many problem");
            // Negative cycle detected
            return -2;
        }
	}

	public Set<RVertex> vertexSet() {
		return graph.vertexSet();
	}
}