import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Arrays;

public class Main {
    // const
    private static final int TRANSFORMATION = -1;

    // data
    private static char[][] sequences;
    private static String[] names;
    private static int[][] memoizer;
    private static int N;
    private static int[][] costs;
    private static int gapCost;

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
        try {
            Scanner sc = new Scanner(new File(file));
            sc.useDelimiter("\r\n");
            String[] charStrings = null;
            // Find chars
            while (sc.hasNext()) {
				String line = sc.next();
				
				// Skip on #
                if(line.startsWith("#")) continue;

                charStrings = line.trim().split("  ");
                break;
            }

            int length = charStrings.length;
            costs = new int[length][length];
            // Find cost
            int i = 0;
            while (i < length) {
                String[] costStrings = sc.next().trim().split(" ");
                int cost[] = Arrays.stream(costStrings)
                        .skip(1)
                        .filter((x) -> !x.equals(" ") && !x.isEmpty())
                        .mapToInt(Integer::valueOf)
                        .map(x -> x * TRANSFORMATION)
                        .toArray();
                costs[i++] = cost;
            }
        } catch(FileNotFoundException e) {
            throw new RuntimeException("Dont wanna deal with it");
        }
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