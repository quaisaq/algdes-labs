package dk.itu.algdes2018.groupi.solver;

import dk.itu.algdes2018.groupi.graph.RGraph;
import dk.itu.algdes2018.groupi.graph.RVertex;

public class NoneSolver implements Solver {
    private static final String NAME = "None";

    private static RGraph preparedGraph(RGraph graph) {

        // Check if direct path between source and target
        for (Object e : graph.getOutgoingEdges(graph.getSource())) {
            RVertex to = graph.getEdgeTo(e);
            if(graph.getTarget() == to){
                RGraph edgeGraph = new RGraph();
                // Add source and target and edge
                edgeGraph.addVertex(graph.getSource());
                edgeGraph.addVertex(graph.getTarget());
                
                edgeGraph.setSource(graph.getSource());
                edgeGraph.setTarget(graph.getTarget());
                edgeGraph.addEdge(graph.getSource(), graph.getTarget());

                return edgeGraph;
            }
        }

        RGraph newGraph = graph.copy();

        // Remove all red vertices, except source and target
        for (RVertex v : graph.getReds()) {
            if(v == graph.getTarget() || v == graph.getSource()){
                continue;
            }
            newGraph.removeVertex(v);
        }

        return newGraph;
    }

    public String solve(RGraph g) {
        RGraph graph = preparedGraph(g);
        int depth = graph.BFSDepth();
        return Integer.toString(depth);
    }

    public String getName() {
        return NAME;
    }
}