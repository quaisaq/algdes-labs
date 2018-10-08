import java.util.Set;

public class Graph {
    private Set<Vertex> vertices;
    private Vertex sink;
    private Vertex source;

    public Vertex getSink() {
        return sink;
    }

    public Vertex getSource() {
        return source;
    }

    public Set<Vertex> getVertices() {
        return vertices;
    }

    public void addVertex(Vertex vertex) {
        vertices.add(vertex);
    }
}