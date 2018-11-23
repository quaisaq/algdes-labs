package dk.itu.algdes2018.groupi.solver;

import dk.itu.algdes2018.groupi.graph.RGraph;
import dk.itu.algdes2018.groupi.graph.RVertex;

public class FewSolver implements Solver {
    private static final String NAME = "Few";

    private static RGraph preparedGraph(RGraph graph) {
        RGraph newGraph = graph.copy();

        for (RVertex v : newGraph.getReds()) {
            for (Object e : newGraph.getIncomingEdges(v)) {
                newGraph.setEdgeWeight(e, 1);
            }
        }

        return newGraph;
    }

    public String solve(RGraph g) {
        RGraph graph = preparedGraph(g);

        int depth = (int)graph.DijkstraWeight();
        return Integer.toString(depth);
    }

    public String getName() {
        return NAME;
    }
}