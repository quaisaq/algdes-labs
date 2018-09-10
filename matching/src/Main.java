
import java.io.*;
import java.util.*;
import java.util.function.*;

public class Main {
    private static HashMap<Integer, String> nameMap;
    
    private static int[][] malePreferences;
    private static int[][] femalePreferences;

    private static int[][] malePreferenceList;
    private static int[][] femalePreferenceList;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please supply the filename to parse");
            System.exit(1);
        }

        nameMap = new HashMap<>();

        int n  = parseInput(args[0]);
        int[] output = galeShapley(n);
        writeOutput(output);
        System.exit(0);
    }

    public static int[] getMaleList(int maleId) {
        return malePreferenceList[getMaleIndex(maleId)];
    }
    
	public static int getMaleIndex(int maleId) {
		return (maleId - 1) / 2;
	}
	
    public static int getMaleId(int maleIndex) {
        return maleIndex * 2 + 1;
    }
    
    public static int[] getFemaleList(int femaleId) {
        return femalePreferenceList[getFemaleIndex(femaleId)];
    }
	
	public static int getFemaleIndex(int femaleId) {
		return femaleId / 2 - 1;
	}
    
    public static int getFemaleId(int femaleIndex) {
        return (femaleIndex + 1) * 2;
    }
    
    /**
     * Reads the file and parses information into malePreferences and femalePreferences.
     */
    public static int parseInput(String filename) {
		int n = -1;
		
		try {
			Scanner sc = new Scanner(new File(filename));
			sc.useDelimiter("\n");
			
			while (sc.hasNext()) {
				String line = sc.next();
				
				// Skip on #
				if(line.startsWith("#")) continue;
				
				// Set n
				if(line.startsWith("n")){
					n = Integer.parseInt(line.split("=")[1]);
					
					malePreferences = new int[n][n];
                    femalePreferences = new int[n][n];
                    
                    malePreferenceList = new int[n][n];
                    femalePreferenceList = new int[n][n];
					
					continue;
				}
				
				String[] words = line.split(" ");
				
				//skip bad lines
				if (words.length < 2) continue;
				
				if (words[0].endsWith(":")) {
					String theNumber = words[0].substring(0, words[0].length() - 1);
					int num = Integer.parseInt(theNumber);
					
					int[] preferences = new int[n];
                    int[] preferencesList = new int[n];
                    
					for (int i = 0; i < n; i++) {
						int prefers = Integer.parseInt(words[i + 1]);
						int prefersIndex = num % 2 == 0 ? getMaleIndex(prefers) : getFemaleIndex(prefers);
                        preferences[prefersIndex] = i;
                        preferencesList[i] = prefersIndex;
					}
					
					if (num % 2 == 0) {
                        int femaleIndex = getFemaleIndex(num);
                        femalePreferences[femaleIndex] = preferences;
                        femalePreferenceList[femaleIndex] = preferencesList;
					}
					else {
                        int maleIndex = getMaleIndex(num);
                        malePreferences[maleIndex] = preferences;
                        malePreferenceList[maleIndex] = preferencesList;
					}
				}
				else {
					int num = Integer.parseInt(words[0]);
					String name = words[1];
					nameMap.put(num, name);
				}
            }
			return n;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		}
    }

    /**
     * Writes the output string.
     * The list of all matches
     */
    public static void writeOutput(int[] output) {
        for (int i = 0; i < output.length; i++) {
            System.out.println(nameMap.get(getMaleId(i)) + " -- " + nameMap.get(getFemaleId(output[i])));
        }
    }
    
    public static int[] galeShapley(int n) {
        int FREE = -1;
        int NO_FREE_MEN = -1;
        int NO_PROPOSALS = -1;
        
        int[] malePreferencePointers = new int[n]; //all malePreferences start at preference index 0
        int[] maleMatches = new int[n];
        int[] femaleMatches = new int[n];
        
        Arrays.fill(maleMatches, FREE); //set all men as FREE
        Arrays.fill(femaleMatches, FREE); //set all women as FREE
        
        Supplier<Integer> getFreeMan = () -> {
            for (int i = 0; i < n; i++) {
                if (maleMatches[i] == FREE) {
                    return i;
                }
            }
            return NO_FREE_MEN;
        };
        
        Function<Integer, Integer> getNextProposal = (maleIndex) -> {
            if (maleIndex == NO_FREE_MEN) {
                return NO_PROPOSALS;
            }
            
            int nextPreference = malePreferencePointers[maleIndex];
            int nextFemale = malePreferenceList[maleIndex][nextPreference];
            return nextPreference == n ? NO_PROPOSALS : nextFemale;
        };
        
        BiFunction<Integer, Integer, Integer> getFemalePreference = (femaleIndex, maleIndex) -> {
            return femalePreferences[femaleIndex][maleIndex];
        };
        
        int freeMan = NO_FREE_MEN;
        int nextProposal = NO_PROPOSALS;
        while ((nextProposal = getNextProposal.apply(freeMan = getFreeMan.get())) != NO_PROPOSALS) {
            if (femaleMatches[nextProposal] == FREE) {
                femaleMatches[nextProposal] = freeMan;
                maleMatches[freeMan] = nextProposal;
            }
            else if (getFemalePreference.apply(nextProposal, freeMan) < getFemalePreference.apply(nextProposal, femaleMatches[nextProposal])) {
                maleMatches[femaleMatches[nextProposal]] = FREE;
                femaleMatches[nextProposal] = freeMan;
                maleMatches[freeMan] = nextProposal;
            }
            malePreferencePointers[freeMan]++;
        }
        
        return maleMatches;
    }
}