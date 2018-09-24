public class Main {
    private static char[][] sequences;
    private static String[] names;
    private static int[][] memoizer;
    private static int N;
    private static int[][] costs;

    public static void main(String[] args) {
        assert args.length == 2;
        parseCost(args[0]);
        parseInput(args[1]);
        alg();
        printOutput();
    }

    public static void parseInput(String file) {
        throw new RuntimeException("Not implemented");
    }

    public static void parseCost(String file) {
        throw new RuntimeException("Not implemented");
    }

    public static void alg() {
        throw new RuntimeException("Not implemented");
    }

    public static void printOutput() {
        throw new RuntimeException("Not implemented");
    }
}