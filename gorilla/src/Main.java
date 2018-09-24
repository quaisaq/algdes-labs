import java.io.*;
import java.util.*;
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
    private static int d;


    private static boolean DEBUG = false;

    public static void main(String[] args) {
        assert args.length == 2;
        parseCost(args[0]);
        parseInput(args[1]);
        for(int i = 0; i < sequences.length; i++){
            for(int j = i+1; sequences.length; j++){
                memoizer = new int[sequences[i].length][sequences[j].length];
                for(int n = 0; n < memoizer.length; n++){
                    Arrays.fill(memoizer[n], -9999);
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
                    }
                    
                    currentSpeciesName = line.substring(1).split(" ")[0];
                    currentGenome = "";
                } else {
                    // Same species, append to currentGenome
                    currentGenome += line;
                }
            }

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

    public static void parseCost(String file) {
        // cost * TRANSFORMATION 
        // Keep it simple stupid
        throw new RuntimeException("Not implemented");
    }



    //skal huske a initialisere memoizer = new int[m+1][n+1];
    public static int algrec(int x, int y, int i, int j){
        /*Result result = new Result();
        result.setName1(names[x]);
        result.setName2(names[y]);*/

        char[] s1 = sequences[x];
        char[] s2 = sequences[y];
        if(memoizer[i][j] != -9999){
            return memoizer[i][j];
        }
        if(i==0 || j==0){
            return d;
        } else {
            int val1 = cost[s1[i]][s2[j]] + algrec(x, y, i-1, j-1);
            int val2 = d + algrec(x, y, i-1, j);
            int val3 = d + algrec(x, y, i, j-1);
            memoizer[i][j] = Math.min(val1, Math.min(val2, val3));

            return memoizer[i][j];
        }
    
        //return result;
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
        result.setCost(memoizer[m][n]);
        
        return result;
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