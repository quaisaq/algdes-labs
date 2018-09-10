import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class pbsolution2
{
	private static boolean DEBUG = false;
	private static boolean ShowRunTime = true;

	public static void main(String[] args){
		long timeStart = System.currentTimeMillis();

		if(args.length == 1){
			// First argument is java file, second argument is filename to parse
			new pbsolution2(args[0]);
		} else{
            System.out.println("Invalid amount of arguments given\njava pbsolution2 fileToParse.txt");
        }

		long timeEnd = System.currentTimeMillis();
		
		// Program run duration in seconds
		double runDuration = (timeEnd - timeStart) * 0.001;
		if(ShowRunTime) System.err.printf("Program complete in: %.3f seconds\n\n", runDuration);
    }
    
    
    private String[] names;
    private int[][] preferences_rated;      // Preference list containing ratings at id corresponding to id of other sex
    private int[][] preferences_ordered;    // Ordered preference list containing ids of the other sex
    private int n;

    public pbsolution2(String filename){
        // Get file scanner for input file
        Scanner sc = getFileScanner(filename);

        // Create data structure (names and preferences arrays)
        parseFileAndCreateDataStructure(sc);

        // Run Gale Shapley algorithm
        int[] marriages = runGaleShapley();

        // Print marriages
        printMarriages(names, marriages);
    }

	private Scanner getFileScanner(String filename)
	{
		if(DEBUG) System.out.println("Trying to read file: " + filename);

		// Does file exist?
		File f = new File(filename);
		if(!f.exists()){
			System.out.printf("File '%s' does not exist. Skipping...\n", filename);
			System.exit(-1);
		}

		// File exists. Return file scanner
		try {
			return new Scanner(f);
		} catch (FileNotFoundException e) {
			System.out.printf("Could not open file '%s'\nError:\n%s", filename, e);
        }

        //This part should never be reached
        System.exit(-1); 
        return null;       
    }
    
    private void parseFileAndCreateDataStructure(Scanner sc){
        sc.useDelimiter(System.getProperty("line.separator"));

        while(sc.hasNext()){
            String line = sc.next();

            // Skip lines starting with # - they are comments
            if(line.startsWith("#")) continue;

            // Definition of n. Instantiate data structure
            if(line.startsWith("n")){
                String[] n_line_parts = line.split("=");
                this.n = Integer.parseInt(n_line_parts[n_line_parts.length-1]);
                this.names = new String[n*2];
                this.preferences_rated = new int[n*2][n*2];
                this.preferences_ordered = new int[n*2][n*2];
                continue;
            }

            String[] line_parts = line.split(" ");
            // Skip empty lines
            if(line_parts.length < 2) continue;

            // ID as a string
            String id_str = line_parts[0];

            // Definition of preferences
            if(id_str.endsWith(":")){
                // Find ID and parse as integer
                id_str = id_str.substring(0, id_str.length()-1);
                int id = Integer.parseInt(id_str)-1;
                
                // Iterate over preferences and set in data structure
                for(int i = 1; i < line_parts.length; i++){
                    int pref_id = Integer.parseInt(line_parts[i])-1;
                    preferences_rated[id][pref_id] = i;     // Set preference value
                    preferences_ordered[id][i*2-1] = pref_id;   // Set preference ordered
                }

            } else {    // Definition of name
                // Find ID and parse as integer
                int id = Integer.parseInt(id_str)-1;

                // Set name
                names[id] = line_parts[1];
            }
        }
    }

    private int[] runGaleShapley(){

        // How many people in total
        int peopleCount = this.n * 2;

        // Array containing current proposals
        int[] proposedTo = new int[peopleCount];
        for(int i = 0; i < peopleCount; i++){
			proposedTo[i] = -1;	// set default value to -1 when not proposed
        }
        
        // Array holding each mans current preference index
        // New array default value = 0
        // Same size as men + women for simplicity
        int[] man_cur_pref_indexs = new int[peopleCount];

        for(int i = 0; i < this.n; i++){
            for(int man_id = 0; man_id < peopleCount; man_id += 2){
                if(DEBUG) System.out.println("Processing man_id: " + man_id);

                // Continue if man already engaged
                if(proposedTo[man_id] >= 0) continue;
                
                // Which index in preferences has current male gotten to?
                int man_cur_pref_index = man_cur_pref_indexs[man_id] + 1;

                // What woman does man want to propose to?
                int man_new_woman_id = this.preferences_ordered[man_id][man_cur_pref_index];
                
                // Who is the woman currently proposed to? -1 for single
                int woman_proposed_to = proposedTo[man_new_woman_id]; 

                if(woman_proposed_to == -1){    // Is woman single?

                    // Set woman and man to proposed
                    proposedTo[man_new_woman_id] = man_id;
                    proposedTo[man_id] = man_new_woman_id;

                } else {                        // Woman already proposed. Challenge
                    if(isNewManBetter(man_new_woman_id, woman_proposed_to, man_id)){

                        // Set womans previous man to single
                        proposedTo[woman_proposed_to] = -1;

                        // Set woman and man to proposed
                        proposedTo[man_new_woman_id] = man_id;
                        proposedTo[man_id] = man_new_woman_id;
                    }
                }
                
                // Update index for current man for next preference to look at
                man_cur_pref_indexs[man_id] += 2;
            }
        }

        return proposedTo;
    }

    // Check whether a new man is better than current man based on preferences
    private boolean isNewManBetter(int woman_id, int cur_man_id, int new_man_id){
        int cur_man_rating = preferences_rated[woman_id][cur_man_id];
        int new_man_rating = preferences_rated[woman_id][new_man_id];

        return new_man_rating < cur_man_rating;
    }

    // Print marriages
    public static void printMarriages(String[] names, int[] proposedTo)
	{
		for(int i = 0; i < names.length; i+=2){
			System.out.printf("%s -- %s\n", names[i], names[proposedTo[i]]);
		}
	}
}
