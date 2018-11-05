public class DiEdge{
    private final Vertex from;
    private final Vertex to;
    private final int weight;

    public DiEdge(Vertex from, Vertex to){
        this(from, to, 0);
    }

    public DiEdge(Vertex from, Vertex to, int weight){
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public Vertex getFrom(){
        return from;
    }

    public Vertex getTo(){
        return to;
    }

    public int getWeight(){
        return weight;
    }
}
