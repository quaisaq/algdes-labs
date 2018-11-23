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

        String filepath = args[0];

        // Parse input file and return graph  
        RGraph graph = Parser.parse(filepath);

        // String list used for printing output after problems are solved
        ResultDto output = new ResultDto();

        // output initial values
        output.Name = filepath;
        output.n = graph.vertexSet().size();

        switch(problem2Solve){
            // Solve None
            case 0:
                output.N = solve(graph, new NoneSolver());
                break;
                
            // Solve Some
            case 1:
                output.S = solve(graph, new SomeSolver());
                break;    
    
            // Solve Many
            case 2:
                output.M = solve(graph, new ManySolver());
                break;          
    
            // Solve Few
            case 3:
                output.F = solve(graph, new FewSolver());
                break;       
    
            // Solve aLtErNaTe
            case 4:
                output.A = solve(graph, new AlternateSolver());
                break;   

            // Solve EVERYTHING!
            default:
                output.A = solve(graph, new AlternateSolver());
                output.F = solve(graph, new FewSolver());
                output.M = solve(graph, new ManySolver());
                output.N = solve(graph, new NoneSolver());
                output.S = solve(graph, new SomeSolver());
                break;               
        }

        //print output
        printOutput(output);
    }

    public static String solve(RGraph g, Solver s) {
        System.err.println("Solving " + s.getName() + "-problem...");
        long startTime = System.nanoTime();

        String output = s.solve(g);

        long endTime = System.nanoTime();
        System.err.printf("Completed in %.2f ms%n", ((endTime - startTime) / 1_000_000D));
        return output;
    }

    public static void printOutput(ResultDto dto){
        System.out.printf("%s, %d, %s, %s, %s, %s, %s%n", dto.Name, dto.n, dto.A, dto.F, dto.M, dto.N, dto.S);
    }

    
}

class ResultDto{
    public ResultDto(){}

    private static final String _default = "-";

    public String Name = _default;
    public int n = 0;
    public String A = _default;     // aLtErNaTe-problem
    public String F = _default;     // Few-problem
    public String M = _default;     // Many-problem
    public String N = _default;     // None-problem
    public String S = _default;     // Some-problem
}
