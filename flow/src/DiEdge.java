public class DiEdge <T> {
    private T value;
    private Vertex to;
    private Vertex from;

    public DiEdge(Vertex from, Vertex to) {
        this.to = to;
        this.from = from;
        from.addEdge(this);
    } 

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Vertex getTo() {
        return to;
    }

    public Vertex getFrom() {
        return from;
    }
}