import java.util.List;
import java.util.ArrayList;

/**
 * A Vertex has a name and a color (black or red).
 * The Vertex knows what edges are going out of, and into it.
 */
public class Vertex{
    private final String name;
    private final boolean red;
    private final List<DiEdge> outEdges;
    private final List<DiEdge> inEdges;

    public Vertex(String name){
        this(name, false);
    }

    public Vertex(String name, boolean red){
        this(name, red, new ArrayList<DiEdge>(), new ArrayList<>());
    }

    public Vertex(String name, boolean red, List<DiEdge> outEdges, List<DiEdge> inEdges){
        this.name = name;
        this.red = red;
        this.outEdges = outEdges;
        this.inEdges = inEdges;
    }

    public String getName(){
        return this.name;
    }

    public boolean isRed(){
        return this.red;
    }

    public void addOutEdge(DiEdge e){
        this.outEdges.add(e);
    }

    public List<DiEdge> getOutEdges(){
        return this.outEdges;
    }

    public void addInEdge(DiEdge e){
        this.inEdges.add(e);
    }

    public List<DiEdge> getInEdges(){
        return this.inEdges;
    }
}