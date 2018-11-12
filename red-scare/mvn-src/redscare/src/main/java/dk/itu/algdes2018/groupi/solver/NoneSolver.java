package dk.itu.algdes2018.groupi.solver;

import dk.itu.algdes2018.groupi.graph.RGraph;
import dk.itu.algdes2018.groupi.graph.RVertex;

public class NoneSolver implements Solver {
    private static final String NAME = "None";

    private static RGraph preparedGraph(RGraph graph) {
        RGraph newGraph = graph.copy();

        for (RVertex v : graph.getReds()) {
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