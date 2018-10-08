public class Main {
    public static void main(String[] args) {
        Graph G = readInput(args[0]);
        MinCut alg = new MinCut();
        Set<DiEdge> cut = alg.find(G);
        printResult(cut);
    }

    public static Graph readInput(String filepath) {
        return null;
    }

    public static void printResult(Set<DiEdge> cut) {
        // print
    }
}