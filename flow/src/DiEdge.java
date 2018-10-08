public class DiEdge <T> {
    private T value;
    private Vertex<T> to;
    private Vertex<T> from;

    public DiEdge(Vertex<T> from, Vertex<T> to) {
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

    public Vertex<T> getTo() {
        return to;
    }

    public Vertex<T> getFrom() {
        return from;
    }
}