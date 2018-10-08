public class Main {
    public static void main(String[] args) {
        FlowGraph<Integer> G = readInput(args[0]);
        MinCut alg = new MinCut();
        Set<DiEdge> cut = alg.find(G);
        printResult(cut);
    }

    public static FlowGraph readInput(String filepath) {
        return null;
    }

    public static void printResult(Set<DiEdge> cut) {
        // print
    }
}