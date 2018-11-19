package dk.itu.algdes2018.groupi.solver;

import dk.itu.algdes2018.groupi.graph.RGraph;
import dk.itu.algdes2018.groupi.graph.RVertex;

public class ManySolver implements Solver {
    private static final String NAME = "Many";

    private static RGraph preparedGraph(RGraph graph) {
        RGraph newGraph = graph.copy();

        for (RVertex v : newGraph.getReds()) {
            for (Object e : newGraph.getIncomingEdges(v)) {
                newGraph.setEdgeWeight(e, -1);
            }
        }

        // for(Object e : newGraph.getEdges()){
        //     double weight = newGraph.getEdgeWeight(e);
        //     RVertex from = newGraph.getEdgeFrom(e);
        //     RVertex to = newGraph.getEdgeTo(e);
        //     System.out.printf("{%s} == {%f} => {%s}%n", from.getName(), weight, to.getName());
        // }

        return newGraph;
    }

    public String solve(RGraph g) {
        RGraph graph = preparedGraph(g);

        int depth = (int)graph.BellmanFordWeight();

        return Integer.toString(depth);
    }

    public String getName() {
        return NAME;
    }
}