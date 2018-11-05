import java.util.List;
import java.util.ArrayList;

public class Main{
    public static boolean DEBUG = false;

    public static void main(String[] args){
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
        DiGraph graph = Parser.parse(args[0]);

        // String list used for printing output after problems are solved
        List<String> output = new ArrayList<>();

        switch(problem2Solve){
            // Solve None
            case 0:
                output.add(noneSolver(graph));
                break;
                
            // Solve Some
            case 1:
                output.add(someSolver(graph));
                break;    
    
            // Solve Many
            case 2:
                output.add(manySolver(graph));
                break;          
    
            // Solve Few
            case 3:
                output.add(fewSolver(graph));
                break;       
    
            // Solve aLtErNaTe
            case 4:
                output.add(aLtErNaTeSolver(graph));
                break;   

            // Solve EVERYTHING!
            default:
                output.add(noneSolver(graph));
                output.add(someSolver(graph));
                output.add(manySolver(graph));
                output.add(fewSolver(graph));
                output.add(aLtErNaTeSolver(graph));
                break;               
        }
        
        printOutput(output);

    }

    public static String noneSolver(DiGraph g){
        System.err.println("Solving None-problem...");
        long startTime = System.nanoTime();

        // TODO do

        long endTime = System.nanoTime();
        if(DEBUG) System.err.printf("Completed in %.3f ms%n", ((endTime - startTime) / 1_000_000D));
        return "";
    }

    public static String someSolver(DiGraph g){
        System.err.println("Solving Some-problem...");
        long startTime = System.nanoTime();

        // TODO do

        long endTime = System.nanoTime();
        if(DEBUG) System.err.printf("Completed in %.3f ms%n", ((endTime - startTime) / 1_000_000D));
        return "";
    }

    public static String manySolver(DiGraph g){
        System.err.println("Solving Many-problem...");
        long startTime = System.nanoTime();

        // TODO do

        long endTime = System.nanoTime();
        if(DEBUG) System.err.printf("Completed in %.3f ms%n", ((endTime - startTime) / 1_000_000D));
        return "";
    }

    public static String fewSolver(DiGraph g){
        System.err.println("Solving Few-problem...");
        long startTime = System.nanoTime();

        // TODO do

        long endTime = System.nanoTime();
        if(DEBUG) System.err.printf("Completed in %.3f ms%n", ((endTime - startTime) / 1_000_000D));
        return "";
    }

    public static String aLtErNaTeSolver(DiGraph g){
        System.err.println("Solving aLtErNaTe-problem...");
        long startTime = System.nanoTime();

        // TODO do

        long endTime = System.nanoTime();
        if(DEBUG) System.err.printf("Completed in %.3f ms%n", ((endTime - startTime) / 1_000_000D));
        return "";
    }

    public static void printOutput(List<String> output){
        for (String s : output) {
            System.out.println(s);
        }
    }
}