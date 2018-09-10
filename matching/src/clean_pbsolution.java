import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;

public class clean_pbsolution
{
	public static void main(String[] args){
		if(args.length > 0){
			// One or more filenames given as input
			for (String filename : args) {
				readTxtFile(filename);
			}
		} else {
			// Read STDIN
			readStdIn();
		}
	}

	public static void readTxtFile(String filename)
	{
		// Does file exist?
		File f = new File(filename);
		if(!f.exists()){
			System.out.printf("File '%s' does not exist. Skipping...\n", filename);
			return;
		}

		// File exists. Run Gale Shapley algorithm
		try {
			Scanner sc = new Scanner(f);
			runGaleShapley(sc);			
		} catch (FileNotFoundException e) {
			System.out.printf("Could not open file '%s'\nError:\n%s", filename, e);
		}
	}

	public static void readStdIn()
	{
		// Run Gale Shapley algorithm
		Scanner sc = new Scanner(System.in);
		runGaleShapley(sc);
	}

	// Returns true if array contains a single person (-1 value)
	public static boolean arrayContainsSingle(int[] arr)
	{
		for(int i = 0; i < arr.length; i++){
			if(arr[i] == -1) return true;
		}
		return false;
	}

	public static void printMarriages(ArrayList<String> names, int[] proposedTo)
	{
		for(int i = 0; i < names.size(); i+=2){
			System.out.printf("%s -- %s\n", names.get(i), names.get(proposedTo[i]));
		}
	}

	public static void runGaleShapley(Scanner sc)
	{
		// Create data structure
		// Note: even indexes are male, odd indexes female
		ArrayList<String> names = new ArrayList<String>();	// names array
		ArrayList<int[]> prefs = new ArrayList<int[]>();	// preferences array
		int n = getDataFromScanner(sc, names, prefs);		// populates names and preferences and returns n
		int peopleCount = names.size();						// How many people found in input

		// Proposed to. Each index represents position in names and prefs arrays. Value is proposed to index.
		int[] proposedTo = new int[peopleCount];
		for(int i = 0; i < proposedTo.length; i++){
			proposedTo[i] = -1;	// set default value to -1 when not proposed
		}

		// Each mans index in prefs array. Default value = 0
		int[] manIndex = new int[(int)(peopleCount * 0.5)];
		
		for(int day = 0; day < n; day++){
			// Iterate over men
			for(int m = 0; m < peopleCount; m+=2)
			{
				// Check if already proposed. If yes, continue
				if(proposedTo[m] >= 0) continue;
				
				// Get preference
				int prefIndex = manIndex[(int)(m*0.5)];
				
				int mansWomanPref = prefs.get(m)[prefIndex];
				
				int womanAlreadyProposedTo = proposedTo[mansWomanPref];
				if(womanAlreadyProposedTo == -1 || isManBetter(prefs, womanAlreadyProposedTo, m, mansWomanPref)){
					if(womanAlreadyProposedTo >= 0){
						// Set old womans proposal status
						proposedTo[womanAlreadyProposedTo] = -1;
					}

					// arrange new proposal
					proposedTo[m] = mansWomanPref;
					proposedTo[mansWomanPref] = m;
				}

				// Increment womenIndex
				manIndex[(int)(m*0.5)] += 1;
			}
		}

		printMarriages(names, proposedTo);
	}

	public static boolean isManBetter(ArrayList<int[]> prefs, int womanAlreadyProposedTo, int manIndex, int womanIndex)
	{
		for (int pref : prefs.get(womanIndex)) {
			if(pref == manIndex) return true;
			else if (pref == womanAlreadyProposedTo) return false;
		}

		// if woman not in proposal list, default true
		// Note, this should not occur
		return true;
	}


	public static void extendArraySizes(ArrayList<String> names, ArrayList<int[]> prefs, int newSize, int prefValueSizes)
	{
		while(newSize > names.size())
		{
			names.add("");
			prefs.add(new int[prefValueSizes]);
		}
	}

	public static int getDataFromScanner(Scanner sc, ArrayList<String> names, ArrayList<int[]> prefs)
	{
		sc.useDelimiter(System.getProperty("line.separator"));

		//Called from runGaleShapley
		int n = 0;		// number of preferences
		
		int arraysCurSize = names.size();

		while(sc.hasNext())
		{
			String s = sc.next();

			// Skip on #
			if(s.startsWith("#")) continue;
			
			// Set n
			if(s.startsWith("n")){
				String[] parts = s.split("=");
				n = Integer.parseInt(parts[parts.length-1]);
				continue;
			}

			String[] parts = s.split(" ");
			if(parts.length < 2) continue;
			else{
				String indexString = parts[0];

				// If index contains : as last char, remove it
				if(indexString.endsWith(":")) indexString = indexString.substring(0, indexString.length() - 1);

				// Set index
				int index = Integer.parseInt(indexString);

				// Extend arrays with default values if index not previously seen
				if(index > arraysCurSize){
					extendArraySizes(names, prefs, index, n);
				}

				// Change index for 0-indexed arrays
				index--;

				if(parts.length == 2){
					// Name declaration
					names.set(index, parts[1]);
					continue;
				} else {
					// parts consists of preferences
					int[] localprefs = new int[n];
					for(int i = 1; i < (n+1); i++){
						localprefs[i-1] = Integer.parseInt(parts[i]) - 1; //Subtract 1, as our arrays are 0-indexed
					}
					prefs.set(index, localprefs);
					continue;
				}
			}
		}
		sc.close();

		return n;
	}
}
