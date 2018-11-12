package dk.itu.algdes2018.groupi.solver;

import dk.itu.algdes2018.groupi.graph.RGraph;
import dk.itu.algdes2018.groupi.graph.RVertex;

public class AlternateSolver implements Solver {
    private static final String NAME = "aLtErNaTe";

    private static RGraph preparedGraph(RGraph graph) {
        RGraph newGraph = graph.copy();
        
        for (Object e : graph.getEdges()) {
            RVertex from = graph.getEdgeFrom(e);
            RVertex to = graph.getEdgeTo(e);
            if (from.isRed() == to.isRed()) {
                newGraph.removeEdge(from, to);
            }
        }

        return newGraph;
    }

    public String solve(RGraph g) {
        RGraph graph = preparedGraph(g);

        int depth = graph.BFSDepth();

        return Boolean.toString(depth != -1);
    }

    public String getName() {
        return NAME;
    }
}