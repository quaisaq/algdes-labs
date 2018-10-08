import java.util.Set;

public class FlowGraph <T> {
    private Vertex<T> sink;
    private Vertex<T> source;

    public Vertex getSink() {
        return sink;
    }

    public Vertex getSource() {
        return source;
    }

    public void setSource(Vertex<T> source) {
        this.source = source;
    }

    public void setSink(Vertex<T> sink) {
        this.sink = sink;
    }
}