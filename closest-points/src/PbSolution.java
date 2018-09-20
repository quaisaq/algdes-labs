import java.util.*;
import java.util.regex.*;
import java.io.*;


public class PbSolution{
    private final static boolean DEBUG = false;
    private final static boolean ShowRunTime = true;

    public static void main(String[] args){
        long timeStart = System.currentTimeMillis();

        if(args.length == 1) {
            String filepath = args[0];

            // Get file scanner
            Scanner fileScanner = getFileScanner(filepath);
    
            // Create data structure
            ArrayList<DataPoint> datapoints = parseFileAndCreateDataStructure(fileScanner);
    
            // Find closest points
            double distance = getClosestPoints(datapoints);
    
            // Print closest points
            printPoints(filepath, datapoints.size(), distance);
        } else{
            System.out.println("Invalid amount of arguments given\njava ClosestPointsDC fileToParse.txt");
        }

        long timeEnd = System.currentTimeMillis();
        
        // Program run duration in seconds
        double runDuration = (timeEnd - timeStart) * 0.001;
        if(ShowRunTime) System.err.printf("Program complete in: %.3f seconds\n\n", runDuration);
    }


    private static Scanner getFileScanner(String filepath){
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


    private static double getClosestPoints(ArrayList<DataPoint> datapoints){
        ArrayList<DataPoint> Px = sortArrayX((ArrayList<DataPoint>)datapoints.clone());
        ArrayList<DataPoint> Py = sortArrayY((ArrayList<DataPoint>)datapoints.clone());

        return getClosestPointsRec(Px, Py);
    }

    private static double getClosestPointsRec(List<DataPoint> Px, List<DataPoint> Py){
        int dpcount = Px.size();
        
        if (dpcount <= 3) {
            double dist = Double.MAX_VALUE;
            
            for(int i = 0; i < dpcount; i++){
                for(int j = i+1; j < dpcount; j++){
                    double tmpDist = calculateDistance(Px.get(i), Px.get(j));
                    if(tmpDist < dist){
                        dist = tmpDist;
                    }
                }
            }
            return dist;
        }

        int splitIndex = dpcount / 2;
        List<DataPoint> Qx = Px.subList(0,splitIndex);
        List<DataPoint> Qy = Py.subList(0,splitIndex);
        List<DataPoint> Rx = Px.subList(splitIndex, dpcount);
        List<DataPoint> Ry = Py.subList(splitIndex, dpcount);

        double distQ = getClosestPointsRec(Qx, Qy);
        double distR = getClosestPointsRec(Rx, Ry);

        double d = Math.min(distQ, distR);
        
        int lxIndex = Qx.size()-1;
        double lx = Px.get(lxIndex).getX();

        double minXValue = lx - d;
        double maxXValue = lx + d;

        int dSplitMinX = lxIndex;
        int dSplitMaxX = lxIndex;

        while(dSplitMinX > 0 && Px.get(dSplitMinX).getX() > minXValue){
            dSplitMinX--;
        }
        while(dSplitMaxX < dpcount && Px.get(dSplitMaxX).getX() < maxXValue){
            dSplitMaxX++;
        }

        ArrayList<DataPoint> Sy = sortArrayY(new ArrayList<DataPoint>(Px.subList(dSplitMinX, dSplitMaxX)));
        
        int SSize = Sy.size();
        for(int i = 0; i < SSize; i++){
            for(int j = i+1; j < j + 15 && j < SSize; j++){
                double tmpDist = calculateDistance(Sy.get(i), Sy.get(j));
                if(tmpDist < d) {
                    d = tmpDist;
                }
            }
        }

        return d;
    }


    private static ArrayList<DataPoint> sortArrayX(ArrayList<DataPoint> datapoints){
        Collections.sort(datapoints, (p1, p2) -> p1.getX() == p2.getX() ? 0 : (p1.getX() < p2.getX() ? -1 : 1));

        for (int i = 0; i < datapoints.size(); i++) {
            DataPoint point = datapoints.get(i);
            point.setXListIndex(i);
        }

        return datapoints;
    }


    private static ArrayList<DataPoint> sortArrayY(ArrayList<DataPoint> datapoints) {
        Collections.sort(datapoints, (p1, p2) -> p1.getY() == p2.getY() ? 0 : (p1.getY() < p2.getY() ? -1 : 1));

        for (int i = 0; i < datapoints.size(); i++) {
            DataPoint point = datapoints.get(i);
            point.setYListIndex(i);
        }
        
        return datapoints;
    }


    private static ArrayList<DataPoint> parseFileAndCreateDataStructure(Scanner sc){
        if(DEBUG) System.out.printf("Parsing file\n");
        
        //String parseRegex = "(?<NAME>\S+)\ +(?<XCOORD>\-?\d+(\.\d+(e\+\d+)?)?)\ +(?<YCOORD>\-?\d+(\.\d+(e\+\d+)?)?)";
        String parseRegex = "(?<NAME>\\S+)\\ +(?<XCOORD>\\-?\\d+(\\.\\d+(e\\+\\d+)?)?)\\ +(?<YCOORD>\\-?\\d+(\\.\\d+(e\\+\\d+)?)?)";
        Pattern parsePattern = Pattern.compile(parseRegex);

        sc.useDelimiter(System.getProperty("line.separator"));

        ArrayList<DataPoint> points = new ArrayList<DataPoint>();
        while(sc.hasNext()){
            String line = sc.next().trim();

            Matcher matcher = parsePattern.matcher(line);
            
            // Check if line matches regex
            if(!matcher.matches()) {
                if(DEBUG) System.out.printf("\tNo regex match on line: %s\n", line);
                continue;
            }

            DataPoint point;

            try{
                double x = Double.parseDouble(matcher.group("XCOORD"));
                double y = Double.parseDouble(matcher.group("YCOORD"));

                // Name parsed last, as x and y might fail
                String name = matcher.group("NAME");

                point = new DataPoint(name, x, y);
            }
            catch(NumberFormatException e){
                continue;   // Line does not contain x and y coordinate
            }

            points.add(point);
        }

        if(DEBUG) System.out.printf("File parsed! Found %d points\n", points.size());
        return points;
    }

    private static void printPoints(String filepath, int pointCount, double distance){
        System.out.printf("%s: %d %f\n", filepath.replace(".txt", "").replace("-tsp", ".tsp"), pointCount, distance);
    }

    private static double calculateDistance(DataPoint p1, DataPoint p2){
        double xDist = Math.abs(p1.getX() - p2.getX());
        double yDist = Math.abs(p1.getY() - p2.getY());
        double distance = Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
        return distance;
    }

    static class DataPoint{
        private String name;
        private double x;
        private double y;
        
        private int xListIndex;
        private int yListIndex;

        public DataPoint(String name, double x, double y){
            this.name = name;
            this.x = x;
            this.y = y;
            xListIndex = -1;
            yListIndex = -1;
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

        public void setXListIndex(int xListIndex) {
            this.xListIndex = xListIndex;
        }

        public void setYListIndex(int yListIndex) {
            this.yListIndex = yListIndex;
        }
        
        public int getXListIndex() {
            return this.xListIndex;
        }

        public int getYListIndex() {
            return this.yListIndex;
        }
    }

}
