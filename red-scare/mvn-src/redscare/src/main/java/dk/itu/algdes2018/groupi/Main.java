package dk.itu.algdes2018.groupi;

import java.util.ArrayList;
import java.util.List;

import dk.itu.algdes2018.groupi.graph.RGraph;
import dk.itu.algdes2018.groupi.solver.AlternateSolver;
import dk.itu.algdes2018.groupi.solver.FewSolver;
import dk.itu.algdes2018.groupi.solver.ManySolver;
import dk.itu.algdes2018.groupi.solver.NoneSolver;
import dk.itu.algdes2018.groupi.solver.Solver;
import dk.itu.algdes2018.groupi.solver.SomeSolver;

public class Main 
{
    public static boolean DEBUG = false;

    public static void main( String[] args )
    {
        if(args.length < 1){
            System.err.println("Please provide input filename as argument");
            System.exit(1);
        }
        
        // Specify which problem to solve as second argument. -1 results in all cases run
        int problem2Solve = (args.length >= 2) ? Integer.parseInt(args[1]) : -1;

        // Debug flag. If 3 arguments given, debug...
        DEBUG = (args.length == 3) ? true : false;

        System.err.println("Hello, let me solve this for you. Please wait :-)");

        // Parse input file and return graph  
        RGraph graph = Parser.parse(args[0]);

        // String list used for printing output after problems are solved
        List<String> output = new ArrayList<>();

        switch(problem2Solve){
            // Solve None
            case 0:
                output.add(solve(graph, new NoneSolver()));
                break;
                
            // Solve Some
            case 1:
                output.add(solve(graph, new SomeSolver()));
                break;    
    
            // Solve Many
            case 2:
                output.add(solve(graph, new ManySolver()));
                break;          
    
            // Solve Few
            case 3:
                output.add(solve(graph, new FewSolver()));
                break;       
    
            // Solve aLtErNaTe
            case 4:
                output.add(solve(graph, new AlternateSolver()));
                break;   

            // Solve EVERYTHING!
            default:
                output.add(solve(graph, new NoneSolver()));
                output.add(solve(graph, new SomeSolver()));
                output.add(solve(graph, new ManySolver()));
                output.add(solve(graph, new FewSolver()));
                output.add(solve(graph, new AlternateSolver()));
                break;               
        }

        //print output
        for (String s : output) {
            System.out.println(s);
        }
    }

    public static String solve(RGraph g, Solver s) {
        System.err.println("Solving " + s.getName() + "-problem...");
        long startTime = System.nanoTime();

        String output = s.solve(g);

        long endTime = System.nanoTime();
        System.err.printf("Completed in %.2f ms%n", ((endTime - startTime) / 1_000_000D));
        return "Result from " + s.getName() + "-problem: " + output;
    }
}
