import java.util.List;

public class DiGraph{
    private final Vertex source;
    private final Vertex target;
    private final List<Vertex> reds;

    public DiGraph(Vertex source, Vertex target, List<Vertex> reds){
        this.source = source;
        this.target = target;
        this.reds = reds;
    }

    public Vertex getSource(){
        return this.source;
    }

    public Vertex getTarget(){
        return this.target;
    }

    public List<Vertex> getReds() {
        return this.reds;
    }
}