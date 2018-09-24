public class Main {
    // const
    private static final int TRANSFORMATION = -1;

    // data
    private static char[][] sequences;
    private static String[] names;
    private static int[][] memoizer;
    private static int N;
    private static int[][] costs;

    public static void main(String[] args) {
        assert args.length == 2;
        parseCost(args[0]);
        parseInput(args[1]);
        Result result = alg();
        printOutput(result);
    }

    public static void parseInput(String file) {
        throw new RuntimeException("Not implemented");
    }

    public static void parseCost(String file) {
        // cost * TRANSFORMATION 
        // Keep it simple stupid
        throw new RuntimeException("Not implemented");
    }

    public static Result alg() {
        throw new RuntimeException("Not implemented");
    }

    public static void printOutput(Result result) {
        // output * TRANSFORMATION
        throw new RuntimeException("Not implemented");
    }

    public static class Result {

    }
}