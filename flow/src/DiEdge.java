public class DiEdge {
    private int value;
    private Vertex to;
    private Vertex from;

    public DiEdge(Vertex from, Vertex to) {
        this.to = to;
        this.from = from;
        from.addEdge(this);
    } 

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Vertex getTo() {
        return to;
    }

    public Vertex getFrom() {
        return from;
    }
}