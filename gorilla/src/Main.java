public class Main {
    // const
    private static final int TRANSFORMATION = -1;

    // data
    private static char[][] sequences;
    private static String[] names;
    private static int[][] memoizer;
    private static int N;
    private static int[][] costs;
    private static int d;

    public static void main(String[] args) {
        assert args.length == 2;
        parseCost(args[0]);
        parseInput(args[1]);
        for(int i = 0; i < sequences.length; i++){
            for(int j = i+1; sequences.length; j++){
                char[] s1 = sequences[i];
                char[] s2 = sequences[j];
                Result result = alg(s1, s2);
                printOutput(result);
            }
        }
        
    }

    public static void parseInput(String file) {
        throw new RuntimeException("Not implemented");
    }

    public static void parseCost(String file) {
        // cost * TRANSFORMATION 
        // Keep it simple stupid
        throw new RuntimeException("Not implemented");
    }

    public static Result alg(char[] s1, char[] s2) {
        int m = s1.length;
        int n = s2.length;
        memoizer = new int[m+1][n+1];

        for(int i = 0; i < m; i++){
            memoizer[i][0] = d;
        }
        for(int j = 0; j < n; j++){
            memoizer[0][j] = d;
        }

        for(int i = 1; i < m; i++){
            for(int j = 1; j < n; j++){
                int val1 = cost[s1[i]][s2[j]] + memoizer[i-1][j-1];
                int val2 = d + memoizer[i-1][j];
                int val3 = d + memoizer[i][j-1]; 
                memoizer[i][j] = Math.min(val1, Math.min(val2, val3));
            }
        }
        return memoizer[m][n];
    }

    public static void printOutput(Result result) {
        // output * TRANSFORMATION
        throw new RuntimeException("Not implemented");
    }

    public static class Result {

    }
}