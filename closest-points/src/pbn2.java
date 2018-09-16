import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Math;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class pbn2
{
    private static boolean DEBUG = false;
    private static boolean ShowRunTime = true;

    public static void main(String[] args){
        long timeStart = System.currentTimeMillis();

		if(args.length == 1){
			// First argument is java file, second argument is filename to parse
			new pbn2(args[0]);
		} else{
            System.out.println("Invalid amount of arguments given\njava pbn2 fileToParse.txt");
        }

		long timeEnd = System.currentTimeMillis();
		
		// Program run duration in seconds
		double runDuration = (timeEnd - timeStart) * 0.001;
		if(ShowRunTime) System.err.printf("\nProgram complete in: %.3f seconds\n\n", runDuration);
    }

    public pbn2(String filepath){
        // Get file scanner
        Scanner fileScanner = getFileScanner(filepath);

        // Create data structure
        ArrayList<pbpoint> datapoints = parseFileAndCreateDataStructure(fileScanner);

        // Find closest points
        pbpointpair closestPoints = getClosestPoints(datapoints);

        // Print closest points
        printPoints(filepath, datapoints.size(), closestPoints);
    }

    private Scanner getFileScanner(String filepath){
        if(DEBUG) System.out.printf("Opening file %s\n", filepath);

        // Does file exist?
		File f = new File(filepath);
		if(!f.exists()){
			System.out.printf("File '%s' does not exist. Skipping...\n", filepath);
			System.exit(-1);
		}

		// File exists. Return file scanner
		try {
            return new Scanner(f);
            
		} catch (FileNotFoundException e) {
            System.out.printf("Could not open file '%s'\nError:\n%s", filepath, e);
            System.exit(-1);
            return null; // line needed to compile
        }
    }

    private ArrayList<pbpoint> parseFileAndCreateDataStructure(Scanner sc){
        if(DEBUG) System.out.printf("Parsing file\n");
        
        //String parseRegex = "(?<NAME>\S+)\ +(?<XCOORD>\-?\d+(\.\d+(e\+\d+)?)?)\ +(?<YCOORD>\-?\d+(\.\d+(e\+\d+)?)?)";
        String parseRegex = "(?<NAME>\\S+)\\ +(?<XCOORD>\\-?\\d+(\\.\\d+(e\\+\\d+)?)?)\\ +(?<YCOORD>\\-?\\d+(\\.\\d+(e\\+\\d+)?)?)";
        Pattern parsePattern = Pattern.compile(parseRegex);

        sc.useDelimiter(System.getProperty("line.separator"));

        ArrayList<pbpoint> points = new ArrayList<pbpoint>();
        while(sc.hasNext()){
            String line = sc.next().trim();

            Matcher matcher = parsePattern.matcher(line);
            
            // Check if line matches regex
            if(!matcher.matches()) {
                if(DEBUG) System.out.printf("\tNo regex match on line: %s\n", line);
                continue;
            }

            pbpoint point;

            try{
                double x = Double.parseDouble(matcher.group("XCOORD"));
                double y = Double.parseDouble(matcher.group("YCOORD"));
                // Name parsed last, as x and y might fail
                String name = matcher.group("NAME");

                point = new pbpoint(name, x, y);
            }
            catch(NumberFormatException e){
                continue;   // Line does not contain x and y coordinate
            }

            points.add(point);
        }

        if(DEBUG) System.out.printf("File parsed! Found %d points\n", points.size());
        return points;
    }

    private pbpointpair getClosestPoints(ArrayList<pbpoint> points){
        int pointCount = points.size();

        pbpoint p1 = new pbpoint("not initialized", 0, 0);
        pbpoint p2 = new pbpoint("not initialized", 0, 0);
        double minDistance = Double.MAX_VALUE;

        for(int i = 0; i < pointCount; i++){
            for(int j = 0; j < pointCount; j++){
                if(i >= j) continue; // we are not interested in double computations
                
                double distance = calculateDistance(points.get(i), points.get(j));
                //if(DEBUG) System.out.printf("\tDistance: %f for points %d and %d\n", distance, i,j);

                if(distance < minDistance){
                    minDistance = distance;
                    p1 = points.get(i);
                    p2 = points.get(j);

                    if(DEBUG) System.out.printf("\t\tNew closest point distance: %f for points %d and %d\n", distance, i, j);
                }
            }
        }

        if(DEBUG) System.out.printf("Closest points found with a distance of %f. Points %s and %s\n", minDistance, p1.getName(), p2.getName());
        return new pbpointpair(p1, p2);
    }

    private void printPoints(String filepath, int pointCount, pbpointpair points){
        //System.out.printf("%s %s %s", filepath, points.getP1().getName(), points.getP2().getName());
        System.out.printf("%s: %d %f\n", filepath.replace(".txt", "").replace("-tsp", ".tsp"), pointCount, calculateDistance(points.getP1(), points.getP2()));
    }

    private double calculateDistance(pbpoint p1, pbpoint p2){
        double xDist = Math.abs(p1.getX() - p2.getX());
        double yDist = Math.abs(p1.getY() - p2.getY());
        double distance = Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
        return distance;
    }


    class pbpointpair{
        private pbpoint p1;
        private pbpoint p2;
        
        public pbpointpair(pbpoint p1, pbpoint p2){
            this.p1 = p1;
            this.p2 = p2;
        }

        public pbpoint getP1(){
            return this.p1;
        }

        public pbpoint getP2(){
            return this.p2;
        }
    }

    class pbpoint{
        private String name;
        private double x;
        private double y;
        public pbpoint(String name, double x, double y){
            this.name = name;
            this.x = x;
            this.y = y;
        }
    
        public String getName(){
            return this.name;
        }
    
        public double getX(){
            return this.x;
        }
    
        public double getY(){
            return this.y;
        }
    }
}
 