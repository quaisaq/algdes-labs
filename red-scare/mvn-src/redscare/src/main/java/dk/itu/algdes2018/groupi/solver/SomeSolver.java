package dk.itu.algdes2018.groupi.solver;

import dk.itu.algdes2018.groupi.graph.RGraph;

public class SomeSolver implements Solver {
    private static final String NAME = "Some";

    private static RGraph preparedGraph(RGraph graph) {
        RGraph newGraph = graph.copy();

        return newGraph;
    }

    public String solve(RGraph graph) {
        //TODO: Put in solution
        return null;
    }

    public String getName() {
        return NAME;
    }
}