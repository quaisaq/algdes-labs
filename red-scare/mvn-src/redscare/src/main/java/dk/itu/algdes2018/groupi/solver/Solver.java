package dk.itu.algdes2018.groupi.solver;

import dk.itu.algdes2018.groupi.graph.RGraph;

public interface Solver {
    public String solve(RGraph graph);
    public String getName();
}