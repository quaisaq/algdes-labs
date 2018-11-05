import java.util.List;
import java.util.ArrayList;

public class Vertex{
    private final String name;
    private final boolean red;
    private final List<DiEdge> edges;

    public Vertex(String name){
        this(name, false);
    }

    public Vertex(String name, boolean red){
        this(name, red, new ArrayList<DiEdge>());
    }

    public Vertex(String name, boolean red, List<DiEdge> edges){
        this.name = name;
        this.red = red;
        this.edges = edges;
    }

    public String getName(){
        return this.name;
    }

    public boolean isRed(){
        return this.red;
    }

    public void addEdge(DiEdge e){
        this.edges.add(e);
    }

    public List<DiEdge> getEdges(){
        return this.edges;
    }
}