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
		System.out.printf("%s--%s: %d%n", result.getName1(), result.getName2(), result.getCost() * TRANSFORMATION);
		System.out.printf("%s%n", result.getSequence1());
		System.out.printf("%s%n", result.getSequence2());
    }

    public static class Result {
		private String sequence1;
		private String sequence2;
		private int cost;
		private String name1;
		private string name2;
		
		public void setSequence1(String sequence1) {
			this.sequence1 = sequence1;
		}
		
		public void setSequence2(String sequence2) {
			this.sequence2 = sequence2;
		}
		
		public void setCost(int cost) {
			this.cost = cost;
		}
		
		public void setName1(String name1) {
			this.name1 = name1;
		}
		
		public void setName2(String name2) {
			this.name2 = name2;
		}
		
		public String getSequence1() {
			return sequence1;
		}
		
		public String getSequence2() {
			return sequence2;
		}
		
		public int getCost() {
			return cost;
		}
		
		public String getName1() {
			return name1;
		}
		
		public String getName2() {
			return name2;
		}
    }
}