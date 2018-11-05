import java.util.List;
import java.util.ArrayList;

public class Vertex{
    private final String name;
    private final List<DiEdge> edges;

    public Vertex(String name){
        this(name, new ArrayList<DiEdge>());
    }

    public Vertex(String name, List<DiEdge> edges){
        this.name = name;
        this.edges = edges;
    }

    public String getName(){
        return this.name;
    }

    public void addEdge(DiEdge e){
        this.edges.add(e);
    }

    public List<DiEdge> getEdges(){
        return this.edges;
    }
}