import java.util.*;
import java.util.regex.*;
import java.io.*;

public class ClosestPointsDC {
    private final static boolean DEBUG = false;
    private final static boolean ShowRunTime = true;

    public static void main(String[] args){
        long timeStart = System.currentTimeMillis();

		if(args.length == 1) {
            String filepath = args[0];

            // Get file scanner
            Scanner fileScanner = getFileScanner(filepath);

            // Create data structure
            ArrayList<PbPoint> datapoints = parseFileAndCreateDataStructure(fileScanner);

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
		if(ShowRunTime) System.err.printf("\nProgram complete in: %.3f seconds\n\n", runDuration);
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

    private static double getClosestPoints(ArrayList<PbPoint> points){
        ArrayList<PbPoint> sortedX = sortListX(points);
        ArrayList<PbPoint> sortedY = sortListY(points);
        double d = getClosestPointsRecursive(sortedX, sortedY);

        if(DEBUG) System.out.printf("Closest points found with a distance of %f. \n", d);
        return d;
    }

    private static ArrayList<PbPoint> sortListX(ArrayList<PbPoint> points) {
        ArrayList<PbPoint> newList = (ArrayList<PbPoint>)points.clone();
        Collections.sort(newList, (p1, p2) -> p1.getX() == p2.getX() ? 0 : (p1.getX() < p2.getX() ? -1 : 1));

        for (int i = 0; i < newList.size(); i++) {
            PbPoint point = newList.get(i);
            point.setXListIndex(i);
        }

        return newList;
    }

    private static ArrayList<PbPoint> sortListY(ArrayList<PbPoint> points) {
        ArrayList<PbPoint> newList = (ArrayList<PbPoint>)points.clone();
        Collections.sort(newList, (p1, p2) -> p1.getY() == p2.getY() ? 0 : (p1.getY() < p2.getY() ? -1 : 1));

        for (int i = 0; i < newList.size(); i++) {
            PbPoint point = newList.get(i);
            point.setYListIndex(i);
        }
        
        return newList;
    }

    private static double getClosestPointsRecursive(List<PbPoint> sortedX, List<PbPoint> sortedY) {
        if (sortedX.size() <= 3) {
            double dist = Double.MAX_VALUE;
            
            for (PbPoint p1 : sortedX) {
                for (PbPoint p2 : sortedX) {
                    if (p1 == p2) continue;

                    double tempDist = calculateDistance(p1, p2);
                    if (tempDist < dist) {
                        dist = tempDist;
                    }
                }
            }

            return dist;
        }

        //otherwise
        DoubleList xLists = splitList(sortedX);

        List<PbPoint> Qx = xLists.getFirst(); 
        List<PbPoint> Rx = xLists.getSecond(); 

        double dist1 = getClosestPointsRecursive(Qx, sortedY);
        double dist2 = getClosestPointsRecursive(Rx, sortedY);

        double d = Math.min(dist1, dist2);
        double lx = Qx.get(Qx.size() - 1).getX();

        HashSet<PbPoint> S = new HashSet<>();

        for (int i = Qx.size() - 1; i >= 0; i--) {
            PbPoint point = Qx.get(i);
            if (lx - point.getX() > d) {
                break;
            }
            S.add(point);
        }

        for (int i = 0; i < Rx.size(); i++) {
            PbPoint point = Rx.get(i);
            if (point.getX() - lx > d) {
                break;
            }
            S.add(point);
        }

        ArrayList<PbPoint> Sy = new ArrayList<>();
        for (PbPoint p : sortedY) {
            if (S.contains(p)) {
                Sy.add(p);
            }
        }

        int attempts = 15;
        for (int i = 0 ; i < Sy.size(); i++) {
            PbPoint p1 = Sy.get(i);
            for (int j = i + 1; j < attempts && j < Sy.size(); j++) {
                PbPoint p2 = Sy.get(j);
                double td = calculateDistance(p1, p2);
                if (td < d) {
                    d = td;
                }
            }
        }

        return d;
    }

    private static DoubleList splitList(List<PbPoint> points) {
        List<PbPoint> half1 = points.subList(0, points.size() / 2);
        List<PbPoint> half2 = points.subList(points.size() / 2, points.size());;
        return new DoubleList(half1, half2);
    }


    private static ArrayList<PbPoint> parseFileAndCreateDataStructure(Scanner sc){
        if(DEBUG) System.out.printf("Parsing file\n");
        
        //String parseRegex = "(?<NAME>\S+)\ +(?<XCOORD>\-?\d+(\.\d+(e\+\d+)?)?)\ +(?<YCOORD>\-?\d+(\.\d+(e\+\d+)?)?)";
        String parseRegex = "(?<NAME>\\S+)\\ +(?<XCOORD>\\-?\\d+(\\.\\d+(e\\+\\d+)?)?)\\ +(?<YCOORD>\\-?\\d+(\\.\\d+(e\\+\\d+)?)?)";
        Pattern parsePattern = Pattern.compile(parseRegex);

        sc.useDelimiter(System.getProperty("line.separator"));

        ArrayList<PbPoint> points = new ArrayList<PbPoint>();
        while(sc.hasNext()){
            String line = sc.next().trim();

            Matcher matcher = parsePattern.matcher(line);
            
            // Check if line matches regex
            if(!matcher.matches()) {
                if(DEBUG) System.out.printf("\tNo regex match on line: %s\n", line);
                continue;
            }

            PbPoint point;

            try{
                double x = Double.parseDouble(matcher.group("XCOORD"));
                double y = Double.parseDouble(matcher.group("YCOORD"));

                // Name parsed last, as x and y might fail
                String name = matcher.group("NAME");

                point = new PbPoint(name, x, y);
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
        //System.out.printf("%s %s %s", filepath, points.getP1().getName(), points.getP2().getName());
        System.out.printf("%s: %d %f\n", filepath.replace(".txt", "").replace("-tsp", ".tsp"), pointCount, distance);
    }

    private static double calculateDistance(PbPoint p1, PbPoint p2){
        double xDist = Math.abs(p1.getX() - p2.getX());
        double yDist = Math.abs(p1.getY() - p2.getY());
        double distance = Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
        return distance;
    }

    static class DoubleList {
        private List<PbPoint> l1;
        private List<PbPoint> l2;

        public DoubleList(List<PbPoint> l1, List<PbPoint> l2) {
            this.l1 = l1;
            this.l2 = l2;
        }

        public List<PbPoint> getFirst() {
            return l1;
        }

        public List<PbPoint> getSecond() {
            return l2;
        }
    }

    static class PbPoint{
        private String name;
        private double x;
        private double y;
        
        private int xListIndex;
        private int yListIndex;

        public PbPoint(String name, double x, double y){
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