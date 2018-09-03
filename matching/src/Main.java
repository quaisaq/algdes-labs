
import java.util.*;
import java.util.function.*;

public class Main {
    private static HashMap<Integer, String> nameMap;
    
    private static int[][] malePreferences;
    private static int[][] femalePreferences;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please supply the filename to parse");
            System.exit(1);
        }
        
        int n  = parseInput(args[0]);
        
        int[] output = galeShapley(n);
        writeOutput(output);
        System.exit(0);
    }

    public static int[] getMaleList(int maleId) {
        return malePreferences[(maleId - 1) / 2];
    }
    
    public static int getMaleId(int maleIndex) {
        return maleIndex * 2 + 1;
    }
    
    public static int[] getFemaleList(int femaleId) {
        return femalePreferences[femaleId / 2 - 1];
    }
    
    public static int getFemaleId(int femaleIndex) {
        return (femaleIndex + 1) * 2;
    }
    
    /**
     * Reads the file and parses information into malePreferences and femalePreferences.
     */
    public static int parseInput(String filename) {
        //BufferedReader br = new BufferedReader(new FileReader(args[0]));
        int n = 2;
        nameMap = new HashMap<>();
        nameMap.put(1, "m");
        nameMap.put(2, "w");
        nameMap.put(3, "m'");
        nameMap.put(4, "w'");
        malePreferences = new int[][] { { 2, 4 }, { 2, 4 } };
        femalePreferences = new int[][] { { 1, 3 }, { 1, 3 } }; 
        return n;
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
            return nextPreference == n ? NO_PROPOSALS : nextPreference;
        };
        
        Function<Integer, Integer, Integer> getFemalePreference = (femaleIndex, maleIndex) -> {
            return Arrays.asList(femalePreferences[femaleIndex]).indexOf(maleIndex);
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