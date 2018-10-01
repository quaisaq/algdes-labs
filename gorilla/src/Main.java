import java.io.*;
import java.util.*;

public class Main {
    // const
    private static final int TRANSFORMATION = -1;
	private static final int DEFAULT_MEMOIZER_VALUE = -99;

    // data
    private static char[][] sequences;
    private static String[] names;
    private static int[][] memoizer;
    private static int N;
    private static int[][] costs;
    private static int d = 4; //We should rename this to gapCost or something

    private static boolean DEBUG = false;

    public static void main(String[] args) {
        assert args.length == 2;
        parseCost(args[0]);
        parseInput(args[1]);
        for(int i = 0; i < sequences.length; i++){
            for(int j = i+1; j < sequences.length; j++){
                memoizer = new int[sequences[i].length][sequences[j].length];
                for(int n = 0; n < memoizer.length; n++){
                    Arrays.fill(memoizer[n], DEFAULT_MEMOIZER_VALUE); //just to fill in something for debugging purposes
                }
                Result result = alg(i, j);
                printOutput(result);
            }
        }
        
    }

    public static void parseInput(String filepath) {
        if(DEBUG) System.out.printf("Opening file %s\n", filepath);

        // Does file exist?
		File f = new File(filepath);
		if(!f.exists()){
			System.out.printf("File '%s' does not exist. Skipping...\n", filepath);
			System.exit(-1);
		}

		try {
            
            Scanner sc = new Scanner(f);

            ArrayList<String> speciesNames = new ArrayList<String>();
            ArrayList<String> speciesGenomes = new ArrayList<String>();

            String currentGenome = "";
            String currentSpeciesName = "";
            int currentSpeciesIndex = 0;

            while (sc.hasNext()) {
                String line = sc.nextLine();
                
                if (line.charAt(0) == '>') {
                    // New species
                    if (currentSpeciesName != "") {
                        // Store previous species + genome
                        speciesNames.add(currentSpeciesName);
                        speciesGenomes.add(currentGenome);
                        currentSpeciesIndex++;
                        currentSpeciesName = "";
                    }
                    
                    currentSpeciesName = line.substring(1).split(" ")[0];
                    currentGenome = "";

                    if (DEBUG) {
                        System.out.printf("New species: %s%n", currentSpeciesName);
                    }
                } else {
                    // Same species, append to currentGenome
                    currentGenome += line;
                }
            }

            speciesNames.add(currentSpeciesName);
            speciesGenomes.add(currentGenome);

            // Convert temporary ArrayLists to arrays.
            N = speciesNames.size();
            if (DEBUG) System.out.println("N is: " + N);
        
            names = new String[N];
            sequences = new char[N][];

            for (int i = 0; i < N; i++) {
                names[i] = speciesNames.get(i);
                sequences[i] = speciesGenomes.get(i).toCharArray();
            }
            // sequences = speciesGenomes.stream().map(str -> str.toCharArray()).toArray();

            assert names.length == sequences.length;
		} catch (FileNotFoundException e) {
            System.out.printf("Could not open file '%s'\nError:\n%s", filepath, e);
            System.exit(-1);
        }
    }

    public static void parseCost(String filepath) {
        if(DEBUG) System.out.printf("Opening file %s\n", filepath);

        // Does file exist?
		File f = new File(filepath);
		if(!f.exists()){
			System.out.printf("File '%s' does not exist. Skipping...\n", filepath);
			System.exit(-1);
        }
        
        try {
            Scanner sc = new Scanner(f);
            sc.useDelimiter("\r\n");
            char[] chars = null;
            int length = -1;

            // Find chars
            while (sc.hasNext()) {
				String line = sc.next();
				
				// Skip on #
                if(line.startsWith("#")) continue;

                String[] charStrings = line.trim().split("  ");
                length = charStrings.length;
                chars = new char[length];
                for (int i = 0; i < charStrings.length; i++) {
                    chars[i] = charStrings[i].trim().charAt(0);
                }
                break;
            }
            
            char greatestChar = 0; //we should always get a greater char than this
            for (int i = 0; i < length; i++) {
                if (chars[i] > greatestChar) {
                    greatestChar = chars[i];
                }
            }

            costs = new int[greatestChar + 1][greatestChar + 1];

            for (int i = 0; i < greatestChar + 1; i++) {
                Arrays.fill(costs[i], -99); //just for visibility, we will never actually hit this in the algorithm
            }

            // Find cost
            for (int i = 0; i < length; i++) {
                String[] costStrings = sc.next().trim().split(" ");
                char c = costStrings[0].trim().charAt(0);

                int k = 1;
                for (int j = 0; j < length; j++, k++) {
                    if (costStrings[k].trim().isEmpty()) {
                        j--;
                        continue;
                    }
                    int cost = Integer.parseInt(costStrings[k]);
                    cost = cost * TRANSFORMATION;
                    costs[c][chars[j]] = cost;
                }
            }

            if (DEBUG) {
                System.out.println("Costs:");
                System.out.printf("%9s", " ");
                for (int i = 'A'; i <= greatestChar; i++) {
                    System.out.printf("[%3c]", (char)i);
                }
                System.out.printf("%n");
                for (int i = 'A'; i < costs.length; i++) {
                    System.out.printf("%3d [%c]: [", i, (char)i);
                    for (int j = 'A'; j < costs[i].length; j++) {
                        System.out.printf(j == 'A' ? "%3d" : ", %3d", costs[i][j]);
                    }
                    System.out.printf("]%n");
                }
            }
        } catch(FileNotFoundException e) {
            System.out.printf("Could not open file '%s'\nError:\n%s", filepath, e);
            System.exit(-1);
        }
    }


    public static Result alg(int x, int y) {
        Result result = new Result();
        result.setName1(names[x]);
        result.setName2(names[y]);

        char[] s1 = sequences[x];
        char[] s2 = sequences[y];
        int m = s1.length;
        int n = s2.length;
        memoizer = new int[m+1][n+1];

        for(int i = 0; i <= m; i++){
            memoizer[i][0] = d * i;
        }
        for(int j = 0; j <= n; j++){
            memoizer[0][j] = d * j;
        }

        if (DEBUG) {
            System.out.println("Algorithm running...");
        }

        for(int i = 1; i <= m; i++){
            for(int j = 1; j <= n; j++){
                int mismatchCost = costs[s1[i-1]][s2[j-1]];
                int val1 = mismatchCost + memoizer[i-1][j-1];
                int val2 = d + memoizer[i-1][j];
                int val3 = d + memoizer[i][j-1]; 
                int smallest = Math.min(val1, Math.min(val2, val3));

                memoizer[i][j] = smallest;

                if (DEBUG) {
                    System.out.printf("i: %d, j: %d%n", i, j);
                    System.out.printf("mism: %d%n", mismatchCost);
                    System.out.printf("val1: %d%n", val1);
                    System.out.printf("val2: %d%n", val2);
                    System.out.printf("val3: %d%n", val3);
                    System.out.printf("memo: ");
                    for (int i2 = 0; i2 <= m; i2++) {
                        System.out.printf(i2 == 0 ? "[" : "      [");
                        for (int j2 = 0; j2 <= n; j2++) {
                            System.out.printf("%3d", memoizer[i2][j2]);
                        }
                        System.out.printf("]%n");
                    }
                }
            }
        }
        result.setCost(memoizer[m][n]);
        
        if (DEBUG) {
            System.out.println("Algorithm done");
        }

        setResultSequences(x, y, memoizer, result);

        return result;
    }

    private static void setResultSequences(int x, int y, int[][] memoizer, Result result) {
        char[] s1 = sequences[x];
        char[] s2 = sequences[y];
        int i = s1.length;
        int j = s2.length;

        int gapCost = d;

        StringBuilder str1 = new StringBuilder();
        StringBuilder str2 = new StringBuilder();

        if (DEBUG) {
            System.out.println("Sequence check running...");
        }
        
        while (i > 0 && j > 0) {
            int mismatchCost = costs[s1[i-1]][s2[j-1]];
            int cost1 = mismatchCost + memoizer[i - 1][j - 1];
            int cost2 = gapCost + memoizer[i - 1][j];
            int cost3 = gapCost + memoizer[i][j - 1];
            int smallest = Math.min(cost1, Math.min(cost2, cost3));

            if (DEBUG) {
                System.out.printf("i: %d, j: %d%n", i, j);
                System.out.printf("cost1: %d%n", cost1);
                System.out.printf("cost2: %d%n", cost2);
                System.out.printf("cost3: %d%n", cost3);
            }

            if (cost1 == smallest) {
                if (DEBUG) {
                    System.out.println("step 1");
                }
                str1.append(s1[i-1]);
                str2.append(s2[j-1]);
                i--;
                j--;
            }
            else if (cost2 == smallest) {
                if (DEBUG) {
                    System.out.println("step 2");
                }
                str1.append(s1[i-1]);
                str2.append('-');
                i--;
            }
            else { //if (cost3 == smallest)
                if (DEBUG) {
                    System.out.println("step 3");
                }
                str1.append('-');
                str2.append(s2[j-1]);
                j--;
            }
        }

        while (i > 0) {
            str1.append(s1[i - 1]);
            str2.append('-');
            i--;
        }

        while (j > 0) {
            str1.append('-');
            str2.append(s2[j - 1]);
            j--;
        }

        if (DEBUG) {
            System.out.println("Sequence check done");
        }

        result.setSequence1(str1.reverse().toString());
        result.setSequence2(str2.reverse().toString());
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
		private String name2;
		
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