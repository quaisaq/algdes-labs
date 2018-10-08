import java.util.Set;

public class FlowGraph {
    private Vertex sink;
    private Vertex source;

    public Vertex getSink() {
        return sink;
    }

    public Vertex getSource() {
        return source;
    }

    public void setSource(Vertex source) {
        this.source = source;
    }

    public void setSink(Vertex sink) {
        this.sink = sink;
    }
}