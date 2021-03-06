package dk.itu.algdes2018.groupi.solver;

import java.util.List;

import dk.itu.algdes2018.groupi.graph.RGraph;
import dk.itu.algdes2018.groupi.graph.RVertex;

public class SomeSolver implements Solver {
    private static final String NAME = "Some";

    private static RGraph firstPrepareGraph(RGraph graph, RVertex red) {
        RGraph newGraph = graph.copy();

        RVertex newSource = new RVertex("NEW SOURCE", false);
        RVertex newTarget = new RVertex("NEW TARGET", false);

        newGraph.addVertex(newSource);
        newGraph.addVertex(newTarget);

        newGraph.addEdge(newSource, newGraph.getSource());
        newGraph.addEdge(newSource, newGraph.getTarget());

        for(Object e : newGraph.getEdges())
            newGraph.setEdgeWeight(e, Double.POSITIVE_INFINITY);

        for(Object e : newGraph.getOutgoingEdges(newSource))
            newGraph.setEdgeWeight(e, 1);

        newGraph.setSource(newSource);
        newGraph.setTarget(newTarget);

        newGraph.addEdge(red, newTarget);

        for(Object e : newGraph.getIncomingEdges(newTarget))
            newGraph.setEdgeWeight(e, 2);

        return newGraph;
    }

    private static void prepareNextGraph(RGraph graph, RVertex oldRed, RVertex newRed) {
        graph.removeEdge(oldRed, graph.getTarget());
        graph.addEdge(newRed, graph.getTarget());

        for(Object e : graph.getIncomingEdges(graph.getTarget()))
            graph.setEdgeWeight(e, 2);
    }

    public String solve(RGraph g) {
        if (!g.isUndirected()) {
            String manyResult = new ManySolver().solve(g);
            if(manyResult.contains("?"))
                return manyResult;
            return Boolean.toString(Integer.parseInt(manyResult) > 0);
        }
        
        List<RVertex> reds = g.getReds();
        if (reds.size() == 0) {
            return Boolean.toString(false);
        }

        RGraph graph = firstPrepareGraph(g, reds.get(0));

        for (int i = 1; i < reds.size(); i++) {
            prepareNextGraph(graph, reds.get(i - 1), reds.get(i));
            double f = graph.maxFlow();
            if (f == 2) {
                return Boolean.toString(true);
            }
        }
        return Boolean.toString(false);
    }

    public String getName() {
        return NAME;
    }
}