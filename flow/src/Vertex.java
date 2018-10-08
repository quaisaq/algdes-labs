import java.util.Set;
import java.util.HashSet;

public class Vertex {
    private int id;
    private String name;
    private Set<DiEdge> edges = new HashSet<DiEdge>();

    public Vertex(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void addEdge(DiEdge e) {
        edges.add(e);
    }

    public Set<DiEdge> getEdges() {
        return edges;
    }
}