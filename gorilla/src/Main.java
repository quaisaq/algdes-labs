import java.io.*;
import java.util.*;

public class Main {
    private static char[][] sequences;
    private static String[] names;
    private static int[][] memoizer;
    private static int N;
    private static int[][] costs;

    private static boolean DEBUG = true;

    public static void main(String[] args) {
        assert args.length == 2;
        parseCost(args[0]);
        parseInput(args[1]);
        alg();
        printOutput();
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
        // * -1 
        // Keep it simple stupid
        throw new RuntimeException("Not implemented");
    }

    public static void alg() {
        throw new RuntimeException("Not implemented");
    }

    public static void printOutput() {
        throw new RuntimeException("Not implemented");
    }
}