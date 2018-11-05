public class DiGraph{
    private final Vertex source;
    private final Vertex target;

    public DiGraph(Vertex source, Vertex target){
        this.source = source;
        this.target = target;
    }

    public Vertex getSource(){
        return this.source;
    }

    public Vertex getTarget(){
        return this.target;
    }
}